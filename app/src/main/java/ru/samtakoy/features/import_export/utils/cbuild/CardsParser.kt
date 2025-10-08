package ru.samtakoy.features.import_export.utils.cbuild

import ru.samtakoy.features.tag.domain.Tag
import java.util.LinkedList
import java.util.Locale
import java.util.regex.Pattern

class CardsParser(tagMap: MutableMap<String, Tag>, nullifyId: Boolean) {
    val cardBuilders: MutableList<CardBuilder>
    private val mCurCardParser: OneCardParser
    private var mQPackId: Long? = null
    private val mNullifyId: Boolean

    init {
        this.cardBuilders = LinkedList<CardBuilder>()
        mCurCardParser = OneCardParser(tagMap)
        mNullifyId = nullifyId
    }

    fun onStart(qPackId: Long) {
        mQPackId = qPackId
    }

    fun onFinalize(qPackId: Long) {
        tryBuildCard()

        for (oneCard in this.cardBuilders) {
            oneCard.qPackId = qPackId
        }
    }

    fun processLine(line: String, lowerLine: String) {
        var line = line
        if (lowerLine.startsWith(CBuilderConst.QUESTION_PREFIX)) {
            tryBuildCard()
            mCurCardParser.openQuestion()

            val regexPattern = "^q:\\[(\\d+)\\](.*)$"
            val r = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE)
            val m = r.matcher(line)


            if (m.find() && m.groupCount() == 2) {
                // update card
                val cardId = m.group(1).toLong()
                line = m.group(2)

                // TODO сначала сравнить по длинне или по символу [
                val trimmedLowerLine = line.lowercase(Locale.getDefault()).trim { it <= ' ' }
                if (trimmedLowerLine == CBuilderConst.CARD_REMOVE_TAG || trimmedLowerLine == CBuilderConst.CARD_REMOVE_TAG2) {
                    // remove card
                    mCurCardParser.markCardToRemove(cardId)
                } else {
                    // update card
                    mCurCardParser.addLine(line, true)
                }


                mCurCardParser.setCardId(if (mNullifyId) CBuilderConst.NO_ID else cardId)
            } else {
                mCurCardParser.addLine(line.substring(CBuilderConst.QUESTION_PREFIX.length), true)
            }
        } else if (mCurCardParser.isCardOpened) {
            if (lowerLine.startsWith(CBuilderConst.ANSWER_PREFIX)) {
                mCurCardParser.openAnswer()
                mCurCardParser.addLine(line.substring(CBuilderConst.ANSWER_PREFIX.length), true)
            } else if (lowerLine.startsWith(CBuilderConst.TAGS_PREFIX)) {
                mCurCardParser.addTagsLine(line)
            } else if (lowerLine.startsWith(CBuilderConst.IMAGE_PREFIX)) {
                mCurCardParser.addImage(line.substring(CBuilderConst.IMAGE_PREFIX.length))
            } else {
                mCurCardParser.addLine(line, false)
            }
        }
    }

    private fun tryBuildCard() {
        if (mCurCardParser.isCardOpened && mCurCardParser.isValidCard) {
            cardBuilders.add(mCurCardParser.getBuilder(mQPackId)!!)
        } else {
            mCurCardParser.reset()
        }
    }

    val isEmpty: Boolean
        get() = cardBuilders.size == 0
}
