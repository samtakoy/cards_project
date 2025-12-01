package ru.samtakoy.importcards.domain.batch.utils.builder

import ru.samtakoy.domain.cardtag.ConcurrentTagMap
import ru.samtakoy.importcards.domain.batch.utils.ImportCardsException
import java.util.Locale

/**
 * @param tagMap общая мапа с сущесствующими и новыми тегами
 * @param nullifyId игнорировать прочитанные айди карт - все карточки как новые
 * */
class QPackBuilder(
    val themeId: Long,
    val srcFilePath: String,
    tagMap: ConcurrentTagMap,
    val fileName: String,
    private val nullifyId: Boolean
) {
    private val mBlockParsers: QPackTopBlocksParser = QPackTopBlocksParser()
    private val mCardsParser: CardsParser = CardsParser(tagMap, nullifyId)
    private var mQPackId: Long = CBuilderConst.NO_ID

    var buildersCount: Int = 1
    var builderNum: Int = 1

    suspend fun addLine(line: String) {
        val lowerLine = line.lowercase(Locale.getDefault())

        if (!mBlockParsers.isFinished) {
            mBlockParsers.processLine(line, lowerLine)
            if (!mBlockParsers.isFinished) {
                return
            } else {
                mQPackId = if (nullifyId) CBuilderConst.NO_ID else this.parsedId
                mCardsParser.onStart(mQPackId)
            }
        }

        mCardsParser.processLine(line, lowerLine)
    }

    @Throws(ImportCardsException::class)
    fun build(): QPackBuilder {
        mCardsParser.onFinalize(mQPackId)

        if (!hasIncomingId() && hasAnyCardId()) {
            throw ImportCardsException(ImportCardsException.Companion.ERR_PACK_ID_MISSING, "")
        }

        return this
    }

    fun hasIncomingId(): Boolean {
        return mBlockParsers.has(CBuilderConst.QPACK_ID_PREFIX)
    }

    val parsedId: Long
        get() = if (hasIncomingId()) {
            mBlockParsers.get(CBuilderConst.QPACK_ID_PREFIX, CBuilderConst.NO_ID_STR)
                .toLong()
        } else {
            CBuilderConst.NO_ID
        }

    val title: String
        get() = mBlockParsers.get(CBuilderConst.TITLE_PREFIX, this.fileName)

    val desc: String
        get() = mBlockParsers.get(CBuilderConst.DESC_PREFIX, "")

    fun hasCreationDate(): Boolean {
        return mBlockParsers.has(CBuilderConst.DATE_PREFIX)
    }

    val creationDate: String
        get() = mBlockParsers.get(CBuilderConst.DATE_PREFIX, DEF_DATE).trim { it <= ' ' }

    fun hasViewCount(): Boolean {
        return mBlockParsers.has(CBuilderConst.VIEWS_PREFIX)
    }

    val viewCount: Int
        get() {
            val stringResult = mBlockParsers.get(CBuilderConst.VIEWS_PREFIX, "0").trim { it <= ' ' }
            return stringResult.toInt()
        }

    val cardBuilders: List<CardBuilder>
        get() = mCardsParser.getCardBuilders()

    fun setTargetQPack(qPackId: Long) {
        mQPackId = qPackId
    }

    fun hasAnyCardId(): Boolean {
        for (cBuilder in this.cardBuilders) {
            if (cBuilder.hasId()) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val DEF_DATE = "01-01-2000 00:00:00"
    }
}