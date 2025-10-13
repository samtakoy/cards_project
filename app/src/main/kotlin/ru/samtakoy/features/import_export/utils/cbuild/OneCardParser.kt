package ru.samtakoy.features.import_export.utils.cbuild

import ru.samtakoy.domain.cardtag.Tag
import ru.samtakoy.domain.cardtag.tagTitleToKey

class OneCardParser(
    private val mTagMap: MutableMap<String, Tag>
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

    fun addTagsLine(tagsLine: String) {
        if (tagsLine.trim { it <= ' ' }.length > 1) {
            val tags = tagsLine.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (tagTitle in tags) {
                // первая будет пустая
                if (tagTitle.length > 0) {
                    addTag(tagTitle.trim { it <= ' ' })
                }
            }
        }

        mIsTextClosed = true
    }

    private fun addTag(tagTitle: String) {
        val key: String = tagTitle.tagTitleToKey()
        var tag = mTagMap.get(key)
        if (tag == null) {
            tag = Tag(id = 0L, title = tagTitle)
            mTagMap.put(key, tag)
        }
        mCurCardBuilder!!.addTag(tag)
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
        //mCurCardBuilder.setQPackId(qPackId);
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
