package ru.samtakoy.importcards.domain.batch.utils.builder

import ru.samtakoy.domain.cardtag.ConcurrentTagMap
import java.util.Locale
import java.util.regex.Pattern

/**
 * @param nullifyId сбросить идентификаторы карточек
 * */
class CardsParser(
    tagMap: ConcurrentTagMap,
    private val nullifyId: Boolean
) {

    private val cardBuilders: MutableList<CardBuilder> = mutableListOf()
    private val mCurCardParser: OneCardParser = OneCardParser(tagMap)
    private var mQPackId: Long? = null


    fun getCardBuilders(): List<CardBuilder> {
        return cardBuilders
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

    suspend fun processLine(line: String, lowerLine: String) {
        var line = line
        if (lowerLine.startsWith(CBuilderConst.QUESTION_PREFIX)) {
            tryBuildCard()
            mCurCardParser.openQuestion()

            val regexPattern = "^q:\\[(\\d+)\\](.*)$"
            val pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(line)

            if (matcher.find() && matcher.groupCount() == 2) {
                // найдена карточка с идентификатором
                val cardId = matcher.group(1).toLong()
                line = matcher.group(2)

                val trimmedLowerLine = line.lowercase(Locale.getDefault()).trim { it.isWhitespace() }
                if (trimmedLowerLine == CBuilderConst.CARD_REMOVE_TAG || trimmedLowerLine == CBuilderConst.CARD_REMOVE_TAG2) {
                    // remove card
                    mCurCardParser.markCardToRemove(cardId)
                } else {
                    // update card
                    mCurCardParser.addLine(line, true)
                }

                mCurCardParser.setCardId(if (nullifyId) CBuilderConst.NO_ID else cardId)
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