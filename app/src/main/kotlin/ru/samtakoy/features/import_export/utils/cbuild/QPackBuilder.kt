package ru.samtakoy.features.import_export.utils.cbuild

import ru.samtakoy.features.qpack.data.DEF_DATE
import ru.samtakoy.features.import_export.utils.ImportCardsException
import ru.samtakoy.features.tag.domain.Tag
import java.util.Locale

class QPackBuilder(
    val themeId: Long,
    val srcFilePath: String,
    tagMap: MutableMap<String, Tag>,
    val fileName: String,
    private val mNullifyId: Boolean
) {
    private val mBlockParsers: BlockParsers
    private val mCardsParser: CardsParser

    private var mQPackId: Long

    var buildersCount: Int = 1
    var builderNum: Int = 1

    init {
        mQPackId = CBuilderConst.NO_ID

        //mTargetQPack = null;
        mBlockParsers = BlockParsers()
        mCardsParser = CardsParser(tagMap, mNullifyId)
    }

    fun addLine(line: String) {
        val lowerLine = line.lowercase(Locale.getDefault())

        if (!mBlockParsers.isFinished) {
            mBlockParsers.processLine(line, lowerLine)
            if (!mBlockParsers.isFinished) {
                return
            } else {
                mQPackId = if (mNullifyId) CBuilderConst.NO_ID else this.parsedId
                mCardsParser.onStart(mQPackId)
            }
        }

        mCardsParser.processLine(line, lowerLine)
    }

    @Throws(ImportCardsException::class) fun build(): QPackBuilder {
        mCardsParser.onFinalize(mQPackId)

        if (!hasIncomingId() && hasAnyCardId()) {
            throw ImportCardsException(ImportCardsException.ERR_PACK_ID_MISSING, "")
        }

        return this
    }

    fun hasIncomingId(): Boolean {
        return mBlockParsers.has(CBuilderConst.QPACK_ID_PREFIX)
    }

    val parsedId: Long
        get() = if (hasIncomingId()) mBlockParsers.get(CBuilderConst.QPACK_ID_PREFIX, CBuilderConst.NO_ID_STR)
            .toLong() else CBuilderConst.NO_ID /**/

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
        get() = mCardsParser.cardBuilders

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
}
