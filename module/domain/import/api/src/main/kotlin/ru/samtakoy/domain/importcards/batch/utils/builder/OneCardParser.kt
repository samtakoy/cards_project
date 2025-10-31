package ru.samtakoy.domain.importcards.batch.utils.builder

import ru.samtakoy.domain.cardtag.ConcurrentTagMap
import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.tagTitleToKey

class OneCardParser(
    private val mTagMap: ConcurrentTagMap
) {

    private val mText: StringBuilder
    private var mCurCardBuilder: CardBuilder? = null

    private var mIsTextClosed = false

    init {
        mText = StringBuilder()
    }

    fun reset() {
        mCurCardBuilder = null
        mText.setLength(0)
        mIsTextClosed = false
    }

    fun openQuestion() {
        reset()
        mCurCardBuilder = CardBuilder()
    }

    fun openAnswer() {
        mCurCardBuilder!!.setQuestion(mText.toString())
        mText.setLength(0)
    }

    suspend fun addTagsLine(tagsLine: String) {
        if (tagsLine.trim { it.isWhitespace() }.length > 1) {
            tagsLine
                .split("#".toRegex())
                .asSequence()
                .map { it.trim { it.isWhitespace() } }
                .filter { it.isNotEmpty() }
                .forEach { tagTitle ->
                    // первая будет пустая
                    if (tagTitle.isNotEmpty()) {
                        addTag(tagTitle.trim { it.isWhitespace() })
                    }
                }
        }

        mIsTextClosed = true
    }

    private suspend fun addTag(tagTitle: String) {
        val key: String = tagTitle.tagTitleToKey()
        mTagMap.addIfNone(key) {
            Tag(id = 0L, title = tagTitle)
        }
        mCurCardBuilder!!.addTagKey(key)
    }

    fun addImage(imageLine: String) {
        var imageLine = imageLine
        imageLine = imageLine.trim { it <= ' ' }
        if (imageLine.length > 1) {
            val images = imageLine.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (img in images) {
                var img = img
                img = img.trim { it <= ' ' }
                if (img.length > 0) {
                    mCurCardBuilder!!.addImage(img)
                }
            }
        }
    }

    fun addLine(line: String?, isFirstLine: Boolean) {
        if (!mIsTextClosed) {
            if (!isFirstLine) {
                mText.append(CBuilderConst.LINE_BREAK)
            }
            mText.append(line)
        }
    }

    fun getBuilder(qPackId: Long?): CardBuilder? {
        if (!this.isCardOpened) {
            return null
        }

        // ?
        // mCurCardBuilder!!.qPackId = qPackId ?: CBuilderConst.NO_ID
        mCurCardBuilder!!.setAnswer(mText.toString())

        val result = mCurCardBuilder

        reset()
        return result
    }

    val isCardOpened: Boolean
        get() = mCurCardBuilder != null

    val isValidCard: Boolean
        get() = mCurCardBuilder != null && mCurCardBuilder!!.isValid

    fun markCardToRemove(cardId: Long) {
        setCardId(cardId)
        mCurCardBuilder!!.toRemove()
    }

    fun setCardId(cardId: Long) {
        mCurCardBuilder!!.cardId = cardId
    }
}