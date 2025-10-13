package ru.samtakoy.features.import_export

import android.content.ContentResolver
import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import kotlinx.coroutines.runBlocking
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.card.domain.model.CardWithTags
import ru.samtakoy.features.import_export.helpers.ZipHelper
import ru.samtakoy.features.import_export.utils.FromUriStreamFactory
import ru.samtakoy.features.import_export.utils.FromZipEntryStreamFactory
import ru.samtakoy.features.import_export.utils.ImportCardsException
import ru.samtakoy.features.import_export.utils.ImportCardsOpts
import ru.samtakoy.features.import_export.utils.StreamFactory
import ru.samtakoy.features.import_export.utils.cbuild.CBuilderConst
import ru.samtakoy.features.import_export.utils.cbuild.CardBuilder
import ru.samtakoy.features.import_export.utils.cbuild.QPackBuilder
import ru.samtakoy.features.import_export.utils.isPackFile
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.domain.theme.Theme
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.Date
import javax.inject.Inject

class ImportApiImpl @Inject constructor(
    val contentResolver: ContentResolver,
    val cardInteractor: CardInteractor,
    val tagInteractor: TagInteractor,
    val cardsRepository: CardsRepository,
    val qPacksRepository: QPacksRepository,
    val themesRepository: ThemesRepository
        //
) : ImportApi {


    override fun loadCardsFromFile(selectedFileUri: Uri, targetThemeId: Long, opts: ImportCardsOpts): Completable {
        // TODO транзакции
        val streamFactory = FromUriStreamFactory(contentResolver, targetThemeId, selectedFileUri)
        return makeCardsBuilderFromFile(streamFactory, opts)
                .filter { qPackBuilder: QPackBuilder -> isAllowedByOpts(qPackBuilder, opts) }
                .map { qPackBuilder: QPackBuilder -> serializePack(qPackBuilder, opts) }
                .map { qPackBuilder: QPackBuilder -> qPackBuilder.build() }
                .flatMap { qPackBuilder: QPackBuilder -> Observable.fromIterable(qPackBuilder.cardBuilders) }
                .map { card: CardBuilder -> serializeCard(card) }
                .ignoreElements()
    }

    override fun batchLoadFromFolder(
            dirPath: String, targetThemeId: Long, opts: ImportCardsOpts
    ): Completable {
        return processImportFromObservables(
                makeQPackBuildersFromPath(contentResolver, dirPath, targetThemeId, opts), opts)
    }

    override fun batchUpdateFromZip(zipFileUri: Uri, opts: ImportCardsOpts): Completable {
        return processImportFromObservables(
                makeQPackBuildersFromZip(contentResolver, zipFileUri, opts), opts)
    }

    // ==========================================================

    private fun linesFromInputStream(
            sf: StreamFactory //InputStream iStream
    ): Observable<String> {
        return object : Observable<String>() {
            override fun subscribeActual(observer: Observer<in String>) {
                var iStream: InputStream? = null
                try {
                    iStream = sf.openStream()
                    val isr = InputStreamReader(iStream, Charset.forName(ExportConst.FILES_CHARSET))
                    val br = BufferedReader(isr)
                    var line = br.readLine()
                    while (line != null) {
                        observer.onNext(line)
                        line = br.readLine()
                    }
                    observer.onComplete()
                } catch (e: Exception) {
                    observer.onError(e)
                } finally {
                    try {
                        iStream?.close()
                    } catch (ignored: Exception) {
                    }
                }
            }
        }
    }

    private fun makeCardsBuilderFromFile(
            streamFactory: StreamFactory,
            opts: ImportCardsOpts
    ): Observable<QPackBuilder> {
        return makeCardsBuilderFromInputStream(streamFactory, opts).toObservable()
    }

    private fun makeCardsBuilderFromInputStream(
            streamFactory: StreamFactory,
            opts: ImportCardsOpts
    ): Single<QPackBuilder> {
        return linesFromInputStream(streamFactory)
                .collect(
                        {
                            QPackBuilder(
                                    streamFactory.themeId,
                                    streamFactory.srcPath,
                                    tagInteractor.buildTagMap().toMutableMap(),
                                    streamFactory.fileName,
                                    opts.nullifyId
                            )
                        }
                ) { obj: QPackBuilder, line: String ->
                    obj.addLine(line)
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

    private fun makeQPackBuildersFromZip(
            resolver: ContentResolver,
            zipFileUri: Uri?,
            opts: ImportCardsOpts
    ): Observable<QPackBuilder> {
        return Observable.fromCallable { resolver.openInputStream(zipFileUri!!) }
                .concatMap { stream: InputStream -> ZipHelper.unzipStream(resolver, stream) }
                .map { streamFactory: FromZipEntryStreamFactory -> actualizeThemes(streamFactory) }
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
                    1
                } //.collectInto(new ArrayList<QPackBuilder>(), (builders, qPackBuilder) -> builders.add(qPackBuilder))
                .toObservable()
                .flatMap(
                        { builders: List<QPackBuilder>? -> Observable.fromIterable(builders) }
                ) { builders: List<QPackBuilder>, qPackBuilder: QPackBuilder ->
                    qPackBuilder.buildersCount = builders.size
                    qPackBuilder
                }
                .filter { qPackBuilder: QPackBuilder -> isAllowedByOpts(qPackBuilder, opts) }
                .map { qPackBuilder: QPackBuilder -> serializePack(qPackBuilder, opts) }
                .map { qPackBuilder: QPackBuilder -> qPackBuilder.build() }
                .concatMap { qPackBuilder: QPackBuilder -> Observable.fromIterable(qPackBuilder.cardBuilders) }
                .map { card: CardBuilder -> serializeCard(card) }
                .ignoreElements()
    }


    @Throws(ImportCardsException::class)
    private fun serializeCard(cardBuilder: CardBuilder): CardWithTags {
        val card = cardBuilder.build()

        if (cardBuilder.hasId()) {
            val existingCardQPackId: Long? = cardsRepository.getCardQPackId(card.card.id)

            existingCardQPackId?.let {
                val existingCardId: Long = card.card.id
                // проверить, что карта из нашего пака
                if (cardBuilder.qPackId != existingCardQPackId) {
                    val errMsg = "card ${existingCardId} from pack ${existingCardQPackId}, not ${cardBuilder.qPackId}, atRemoving: ${cardBuilder.isToRemove}"
                    throw ImportCardsException(ImportCardsException.ERR_WRONG_CARD_PACK, errMsg)
                }

                if (cardBuilder.isToRemove) {
                    // удалить
                    cardInteractor.deleteCardWithRelationsSync(existingCardId)
                    return card
                }
            }

            if (existingCardQPackId != null) {
                // обновить
                cardsRepository.updateCardSync(card.card)
                // будет обновлено ниже
                tagInteractor.deleteAllTagsFromCard(cardBuilder.cardId)
            } else {
                // создать
                val cardId = cardsRepository.addCardSync(card.card)
                cardBuilder.cardId = cardId
            }

        } else {

            val cardId = cardsRepository.addCardSync(card.card)
            cardBuilder.cardId = cardId
        }

        serializeCardTags(cardBuilder.cardId, card)

        return card.copy(
            card = card.card.copy(
                id = cardBuilder.cardId
            )
        )
    }

    private fun serializeCardTags(cardId: Long, card: CardWithTags) {
        serializeNewTags(card.tags)
        tagInteractor.addCardTags(cardId, card.tags.map{ it.id })
    }

    private fun serializeNewTags(tags: List<Tag>) {
        tags.forEach { serializeIfNewTag(it) }
    }

    private fun serializeIfNewTag(tag: Tag): Tag {
        return if (tag.id == 0L) {
            tag.copy(
                id = tagInteractor.addTag(tag)
            )
        } else {
            tag
        }
    }

    @Throws(ImportCardsException::class)
    private fun serializePack(
            qPackBuilder: QPackBuilder,
            opts: ImportCardsOpts
    ): QPackBuilder {

        val creationDate: Date = if (qPackBuilder.hasCreationDate()) {
            try {
                DateUtils.DATE_FORMAT.parse(qPackBuilder.creationDate)!!
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
                qPacksRepository.updateQPack(qPack);
                qPack.id
            } else {
                // создать новый, если разрешено
                if (opts.isAllowWithIdCreation) {
                    qPacksRepository.addQPack(qPack)
                } else {
                    throw ImportCardsException(ImportCardsException.ERR_PACK_WITH_ID_CREATION_NOT_ALLOWED, "")
                }
            }
        } else {
            qPacksRepository.addQPack(qPack)
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
            val childTheme: Theme? = themesRepository.getThemeWithTitle(parentThemeId, f.name)
            childThemeId = if (childTheme != null) {
                childTheme.id
            } else {
                runBlocking {
                    themesRepository.addNewTheme(parentThemeId, f.name)?.id ?: 0L
                }
            }
            return Observable.fromArray(*f.listFiles()).flatMap { file: File -> listFiles(resolver, file, childThemeId, opts) }
        }
        if (!isPackFile(f)) {
            return Observable.empty()
        }
        val streamFactory = FromUriStreamFactory(resolver, parentThemeId, Uri.fromFile(f))
        return makeCardsBuilderFromFile(streamFactory, opts) //.toObservable();
    }


    private fun actualizeThemes(streamFactory: FromZipEntryStreamFactory): FromZipEntryStreamFactory {
        val themesList: List<String> = streamFactory.themesPath
        if (themesList.size == 0) {
            // hasnt parent theme
            streamFactory.themeId = CBuilderConst.NO_ID
            return streamFactory
        }
        var parentThemeId = CBuilderConst.NO_ID
        var newTheme = false
        //val resolver = streamFactory.resolver
        for (themeName in themesList) {
            val theme: Theme? = if (newTheme) null else themesRepository.getThemeWithTitle(parentThemeId, themeName)
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