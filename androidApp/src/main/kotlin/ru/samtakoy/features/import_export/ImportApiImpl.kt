package ru.samtakoy.features.import_export

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.rxObservable
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.theme.Theme
import ru.samtakoy.features.import_export.helpers.ZipHelper
import ru.samtakoy.features.import_export.utils.streams.FromUriStreamFactory
import ru.samtakoy.features.import_export.utils.streams.FromZipEntryStreamFactory
import ru.samtakoy.importcards.domain.batch.utils.ImportCardsException
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.features.import_export.utils.streams.StreamFactory
import ru.samtakoy.importcards.domain.batch.utils.builder.CBuilderConst
import ru.samtakoy.importcards.domain.batch.utils.builder.CardBuilder
import ru.samtakoy.importcards.domain.batch.utils.builder.QPackBuilder
import ru.samtakoy.features.import_export.utils.isPackFile
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class ImportApiImpl(
    val context: Context,
    val cardInteractor: CardInteractor,
    val tagInteractor: TagInteractor,
    val cardsRepository: CardsRepository,
    val qPacksRepository: QPacksRepository,
    val themesRepository: ThemesRepository
) : ImportApi {

    private val contentResolver: ContentResolver
        get() = context.contentResolver

    override fun loadCardsFromFile(selectedFileUri: Uri, targetThemeId: Long, opts: ImportCardsOpts): Completable {
        // TODO транзакции
        val streamFactory = FromUriStreamFactory(contentResolver, targetThemeId, selectedFileUri)
        return makeCardsBuilderFromFile(streamFactory, opts)
            .filter { qPackBuilder: QPackBuilder -> isAllowedByOpts(qPackBuilder, opts) }
            .map { qPackBuilder: QPackBuilder -> runBlocking { serializePack(qPackBuilder, opts) } }
            .map { qPackBuilder: QPackBuilder -> qPackBuilder.build() }
            // TODO сохранение новых тегов
            .concatMap { qPackBuilder: QPackBuilder -> Observable.fromIterable(qPackBuilder.cardBuilders) }
            .map { card: CardBuilder -> runBlocking { serializeCard(card) } }
            .ignoreElements()
    }

    override fun batchLoadFromFolder(
        dirPath: String, targetThemeId: Long, opts: ImportCardsOpts
    ): Completable {
        return processImportFromObservables(
            makeQPackBuildersFromPath(contentResolver, dirPath, targetThemeId, opts), opts
        )
    }

    override fun batchUpdateFromZip(zipFileUri: Uri, opts: ImportCardsOpts): Completable {
        return processImportFromObservables(
            qpBuilders = makeQPackBuildersFromZip(contentResolver, zipFileUri, opts),
            opts = opts
        )
    }

    // ==========================================================

    private fun linesFromInputStream(
        sf: StreamFactory //InputStream iStream
    ): Flow<String> {
        return flow {
            var iStream: InputStream? = null
            try {
                iStream = sf.openStream()
                val isr = InputStreamReader(iStream, Charset.forName(ExportConst.FILES_CHARSET))
                val br = BufferedReader(isr)
                var line = br.readLine()
                while (line != null) {
                    emit(line)
                    line = br.readLine()
                }
            } finally {
                try {
                    iStream?.close()
                } catch (ignored: Exception) {
                }
            }
        }
    }

    private fun makeCardsBuilderFromFile(
        streamFactory: StreamFactory,
        opts: ImportCardsOpts
    ): Observable<QPackBuilder> {
        return makeCardsBuilderFromInputStream(streamFactory, opts)
    }

    private fun makeCardsBuilderFromInputStream(
        streamFactory: StreamFactory,
        opts: ImportCardsOpts
    ): Observable<QPackBuilder> {
        return rxObservable {
            QPackBuilder(
                streamFactory.themeId,
                streamFactory.srcPath,
                // TODO TagMap + common object
                tagInteractor.buildTagMap(),
                streamFactory.fileName,
                opts.nullifyId
            ).also { qPackBuilder ->
                linesFromInputStream(streamFactory).collect {
                    qPackBuilder.addLine(it)
                }
            }
        }
    }

    private fun isAllowedByOpts(qPackBuilder: QPackBuilder, opts: ImportCardsOpts): Boolean {
        return if (qPackBuilder.hasIncomingId()) {
            // с id
            opts.isAllowWithIdProcessing
        } else {
            // без id
            opts.isAllowNewImporting
        }
    }

    // TODO remove
    private fun makeQPackBuildersFromZip(
        resolver: ContentResolver,
        zipFileUri: Uri?,
        opts: ImportCardsOpts
    ): Observable<QPackBuilder> {
        return Observable.fromCallable { resolver.openInputStream(zipFileUri!!) }
            .concatMap { stream: InputStream -> ZipHelper.unzipStream(stream) }
            // актуализиривать темы в БД
            .map { streamFactory: FromZipEntryStreamFactory -> actualizeThemes(streamFactory) }
            // TODO создать парсеры QPack`ов
            .concatMap { streamFactory: FromZipEntryStreamFactory -> makeCardsBuilderFromFile(streamFactory, opts) }
    }

    private fun makeQPackBuildersFromPath(
        resolver: ContentResolver,
        dirPath: String,
        targetThemeId: Long,
        opts: ImportCardsOpts
    ): Observable<QPackBuilder> {
        return Observable.fromArray(*File(dirPath).listFiles())
            .flatMap { f1: File -> listFiles(resolver, f1, targetThemeId, opts) }
    }

    private fun processImportFromObservables(
        qpBuilders: Observable<QPackBuilder>,
        opts: ImportCardsOpts
    ): Completable {
        return qpBuilders.scan { prevBuilder: QPackBuilder, nextBuilder: QPackBuilder ->
            nextBuilder.builderNum = prevBuilder.builderNum + 1
            nextBuilder
        }
            .toSortedList { aBuilder: QPackBuilder, bBuilder: QPackBuilder ->
                if (aBuilder.hasIncomingId() == bBuilder.hasIncomingId()) {
                    return@toSortedList 0
                }
                if (aBuilder.hasIncomingId()) {
                    // с id раньше обрабатываются
                    return@toSortedList -1
                }
                if (bBuilder.hasIncomingId()) {
                    // с id раньше обрабатываются
                    return@toSortedList 1
                }
                0
            } //.collectInto(new ArrayList<QPackBuilder>(), (builders, qPackBuilder) -> builders.add(qPackBuilder))
            .toObservable()
            .flatMap(
                { builders: List<QPackBuilder>? -> Observable.fromIterable(builders) }
            ) { builders: List<QPackBuilder>, qPackBuilder: QPackBuilder ->
                qPackBuilder.buildersCount = builders.size
                qPackBuilder
            }
            .filter { qPackBuilder: QPackBuilder -> isAllowedByOpts(qPackBuilder, opts) }

            .map { qPackBuilder: QPackBuilder -> runBlocking { serializePack(qPackBuilder, opts) } }
            .map { qPackBuilder: QPackBuilder -> qPackBuilder.build() }
            // TODO сохранение новых тегов
            .concatMap { qPackBuilder: QPackBuilder -> Observable.fromIterable(qPackBuilder.cardBuilders) }
            .map { card: CardBuilder -> runBlocking { serializeCard(card) } }
            .ignoreElements()
    }

    @Throws(ImportCardsException::class)
    private suspend fun serializeCard(cardBuilder: CardBuilder): Card {
        val card = cardBuilder.build()

        if (cardBuilder.hasId()) {
            val existingCardQPackId: Long? = cardsRepository.getCardQPackId(card.id)

            existingCardQPackId?.let {
                val existingCardId: Long = card.id
                // проверить, что карта из нашего пака
                if (cardBuilder.qPackId != existingCardQPackId) {
                    val errMsg =
                        "card ${existingCardId} from pack ${existingCardQPackId}, not ${cardBuilder.qPackId}, atRemoving: ${cardBuilder.isToRemove}"
                    throw ImportCardsException(ImportCardsException.ERR_WRONG_CARD_PACK, errMsg)
                }

                if (cardBuilder.isToRemove) {
                    // удалить
                    cardInteractor.deleteCardWithRelations(existingCardId)
                    return card
                }
            }

            if (existingCardQPackId != null) {
                // обновить
                cardsRepository.updateCard(card)
                // будет обновлено ниже
                tagInteractor.deleteAllTagsFromCard(cardBuilder.cardId)
            } else {
                // создать
                val cardId = runBlocking { cardsRepository.addCard(card) }
                cardBuilder.cardId = cardId
            }

        } else {

            val cardId = runBlocking { cardsRepository.addCard(card) }
            cardBuilder.cardId = cardId
        }

        /* TODO есть в новой версии (ImportCardsZipUseCaseImpl), тут закоментировано
        serializeCardTags(
            cardBuilder.cardId,
            cardBuilder.
        )*/

        return card.copy(
            id = cardBuilder.cardId
        )
    }

    private suspend fun serializeCardTags(cardId: Long, tags: List<Tag>) {
        tagInteractor.addCardTags(
            cardId = cardId,
            tagIds = serializeNewTags(tags).map { it.id }
        )
    }

    private suspend fun serializeNewTags(tags: List<Tag>): List<Tag> {
        return tags.map { serializeIfNewTag(it) }
    }

    private suspend fun serializeIfNewTag(tag: Tag): Tag {
        return if (tag.id == 0L) {
            tag.copy(
                id = tagInteractor.addTag(tag)
            )
        } else {
            tag
        }
    }

    @OptIn(ExperimentalTime::class)
    @Throws(ImportCardsException::class)
    private suspend fun serializePack(
        qPackBuilder: QPackBuilder,
        opts: ImportCardsOpts
    ): QPackBuilder {

        val creationDate: Instant = if (qPackBuilder.hasCreationDate()) {
            try {
                DateUtils.parseToDate(qPackBuilder.creationDate)!!
            } catch (e: Throwable) {
                DateUtils.currentTimeDate
            }
        } else {
            DateUtils.currentTimeDate
        }
        val qPack = QPack(
            id = qPackBuilder.parsedId,
            themeId = qPackBuilder.themeId,
            path = qPackBuilder.srcFilePath,
            fileName = qPackBuilder.fileName,
            title = qPackBuilder.title,
            desc = qPackBuilder.desc,
            creationDate = creationDate,
            viewCount = if (qPackBuilder.hasViewCount()) {
                qPackBuilder.viewCount
            } else {
                0
            },
            lastViewDate = creationDate,
            favorite = 0
        )

        val resultPackId = if (qPackBuilder.hasIncomingId()) {
            if (qPacksRepository.isPackExists(qPackBuilder.parsedId)) {
                // обновить
                runBlocking { qPacksRepository.updateQPack(qPack) }
                qPack.id
            } else {
                // создать новый, если разрешено
                if (opts.isAllowWithIdCreation) {
                    runBlocking { qPacksRepository.addQPack(qPack) }
                } else {
                    throw ImportCardsException(ImportCardsException.ERR_PACK_WITH_ID_CREATION_NOT_ALLOWED, "")
                }
            }
        } else {
            runBlocking { qPacksRepository.addQPack(qPack) }
        }

        qPackBuilder.setTargetQPack(resultPackId)
        return qPackBuilder
    }

    private fun isPackFile(file: File): Boolean {
        return isPackFile(file.name)
    }

    private fun isValidThemeFolderName(folderName: String): Boolean {
        return folderName.indexOf(".") != 0
    }

    private fun listFiles(
        resolver: ContentResolver,
        f: File,
        parentThemeId: Long,
        opts: ImportCardsOpts
    ): Observable<QPackBuilder>? {
        if (f.isDirectory) {
            if (!isValidThemeFolderName(f.name)) {
                return Observable.empty()
            }

            // создать или получить соответствуюущую дирректорию в базе
            // получить из базы - дочернюю с тем же именем, как дирректория
            // если нет - создать и ее id транслировать ниже
            val childThemeId: Long
            val childTheme: Theme? = runBlocking {
                themesRepository.getThemeWithTitle(parentThemeId, f.name)
            }
            childThemeId = if (childTheme != null) {
                childTheme.id
            } else {
                runBlocking {
                    themesRepository.addNewTheme(parentThemeId, f.name)?.id ?: 0L
                }
            }
            return Observable.fromArray(*f.listFiles())
                .flatMap { file: File -> listFiles(resolver, file, childThemeId, opts) }
        }
        if (!isPackFile(f)) {
            return Observable.empty()
        }
        val streamFactory = FromUriStreamFactory(resolver, parentThemeId, Uri.fromFile(f))
        return makeCardsBuilderFromFile(streamFactory, opts) //.toObservable();
    }

    // TODO remove и посмотреть, почему для импорта из папки такого нет
    private fun actualizeThemes(streamFactory: FromZipEntryStreamFactory): FromZipEntryStreamFactory {
        val themesList: List<String> = streamFactory.themesPath
        if (themesList.size == 0) {
            // hasnt parent theme
            streamFactory.themeId = CBuilderConst.NO_ID
            return streamFactory
        }
        var parentThemeId = CBuilderConst.NO_ID
        var newTheme = false

        for (themeName in themesList) {
            val theme: Theme? = runBlocking {
                if (newTheme) null else themesRepository.getThemeWithTitle(parentThemeId, themeName)
            }
            if (theme != null) {
                parentThemeId = theme.id
            } else {
                newTheme = true
                parentThemeId = runBlocking {
                    themesRepository.addNewTheme(parentThemeId, themeName)?.id ?: 0L
                }
            }
        }
        streamFactory.themeId = parentThemeId
        return streamFactory
    }

}