package ru.samtakoy.domain.importcards.batch.utils.builder

import ru.samtakoy.domain.card.CardConst
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.qpack.QPackConst

class CardBuilder {
    var qPackId: Long = QPackConst.NO_ID
    var cardId: Long = CardConst.NO_ID
    private var mQuestion = ""
    private var mAnswer: String = ""
    private val mImages = mutableListOf<String>()
    private val mTagKeys = mutableSetOf<String>()

    var isToRemove: Boolean = false
        private set

    fun build(): Card {
        return Card(
            this.cardId,
            this.qPackId,
            mQuestion,
            mAnswer,
            mImages,
            "",
            0
        )
    }

    fun setQuestion(text: String) {
        mQuestion = text
    }

    fun setAnswer(text: String) {
        mAnswer = text
    }

    fun addTagKey(tagKey: String) {
        mTagKeys.add(tagKey)
    }

    fun getTagKeys(): Set<String> {
        return mTagKeys
    }

    fun addImage(imageName: String) {
        mImages.add(imageName)
    }

    fun toRemove() {
        this.isToRemove = true
    }

    val isValid: Boolean
        get() {
            if (this.isToRemove || this.cardId !== CardConst.NO_ID) {
                return true
            }

            // check question emptiness
            val checkStrings =
                mQuestion.split(CBuilderConst.LINE_BREAK).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (str in checkStrings) {
                if (str.trim { it.isWhitespace() }.isNotEmpty()) {
                    return true
                }
            }
            return false
        }

    fun hasId(): Boolean {
        return this.cardId != CardConst.NO_ID
    }

    fun hasQPackId(): Boolean {
        return this.qPackId != QPackConst.NO_ID
    }
}