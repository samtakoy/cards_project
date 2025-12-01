package ru.samtakoy.importcards.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.importcards.data.model.ExportImportConst
import ru.samtakoy.importcards.data.model.QPackSource
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.cardtag.ConcurrentTagMap
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.importcards.domain.batch.utils.ImportCardsException
import ru.samtakoy.importcards.domain.batch.utils.builder.CardBuilder
import ru.samtakoy.importcards.domain.batch.utils.builder.QPackBuilder
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.theme.ThemeInteractor
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

internal class QPackBuilderInteractorImpl(
    private val themeInteractor: ThemeInteractor,
    private val qPackInteractor: QPackInteractor,
    private val cardInteractor: CardInteractor,
    private val tagInteractor: TagInteractor
) {
    suspend fun actualizeThemePaths(themePath: List<String>): Long {
        return themeInteractor.actualizeThemePath(themePath = themePath)
    }

    suspend fun createQPackBuilder(
        source: QPackSource,
        allTagMap: ConcurrentTagMap,
        opts: ImportCardsOpts
    ): QPackBuilder {
        return QPackBuilder(
            source.themeId,
            source.srcPath,
            allTagMap,
            source.fileName,
            opts.nullifyId
        ).also { qPackBuilder ->
            linesFromSource(source).collect {
                qPackBuilder.addLine(it)
            }
        }
    }

    fun isAllowedByOpts(qPackBuilder: QPackBuilder, opts: ImportCardsOpts): Boolean {
        return if (qPackBuilder.hasIncomingId()) {
            // с id
            opts.isAllowWithIdProcessing
        } else {
            // без id
            opts.isAllowNewImporting
        }
    }

    @OptIn(ExperimentalTime::class)
    @Throws(ImportCardsException::class)
    suspend fun saveQPackToDatabase(
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
            if (qPackInteractor.getQPack(qPackBuilder.parsedId) != null) {
                // обновить
                qPackInteractor.updateQPack(qPack);
                qPack.id
            } else {
                // создать новый, если разрешено
                if (opts.isAllowWithIdCreation) {
                    qPackInteractor.addQPack(qPack)
                } else {
                    throw ImportCardsException(ImportCardsException.ERR_PACK_WITH_ID_CREATION_NOT_ALLOWED, "")
                }
            }
        } else {
            qPackInteractor.addQPack(qPack)
        }
        qPackBuilder.setTargetQPack(resultPackId)
        return qPackBuilder
    }

    @Throws(ImportCardsException::class)
    suspend fun saveCardToDatabase(cardBuilder: CardBuilder, tagMap: ConcurrentTagMap): Card {
        val card = cardBuilder.build()

        if (cardBuilder.hasId()) {
            val existingCardQPackId: Long? = cardInteractor.getCardQPackId(card.id)

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
                cardInteractor.updateCard(card)
                // будет обновлено ниже
                tagInteractor.deleteAllTagsFromCard(cardBuilder.cardId)
            } else {
                // создать
                val cardId = cardInteractor.addCard(card)
                cardBuilder.cardId = cardId
            }

        } else {
            val cardId = cardInteractor.addCard(card)
            cardBuilder.cardId = cardId
        }

        tagInteractor.addCardTags(
            cardId = cardBuilder.cardId,
            tagIds = tagMap.getByKeys(cardBuilder.getTagKeys().toList()).map { it.id }
        )

        return card.copy(
            id = cardBuilder.cardId
        )
    }

    private fun linesFromSource(
        source: QPackSource
    ): Flow<String> {
        return flow {
            var iStream: InputStream? = null
            try {
                iStream = source.content.inputStream()
                val isr = InputStreamReader(iStream, Charset.forName(ExportImportConst.FILES_CHARSET))
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
}