package ru.samtakoy.features.import_export.utils.cbuild

import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.card.domain.model.CardWithTags
import ru.samtakoy.domain.cardtag.Tag

class CardBuilder {
    var qPackId: Long
    var cardId: Long
    private var mQuestion = ""
    private var mAnswer: String = ""
    private val mImages: MutableList<String>
    private val mTags: MutableList<Tag>

    var isToRemove: Boolean = false
        private set

    init {
        this.qPackId = CBuilderConst.NO_ID
        this.cardId = CBuilderConst.NO_ID

        mImages = ArrayList<String>()
        mTags = ArrayList<Tag>()
    }

    fun build(): CardWithTags {
        val result: CardWithTags = CardWithTags(
            Card(
                this.cardId,
                this.qPackId,
                mQuestion,
                mAnswer,
                mImages,
                "",
                0
            ),
            mTags
        )
        return result
    }

    fun setQuestion(text: String) {
        mQuestion = text
    }

    fun setAnswer(text: String) {
        mAnswer = text
    }

    fun addTag(tag: Tag) {
        mTags.add(tag)
    }

    fun addImage(imageName: String) {
        mImages.add(imageName)
    }

    /*public boolean isEmpty(){
        return mQuestion.length()==0 && mAnswer.length()==0;
    }/ **/
    fun toRemove() {
        this.isToRemove = true
    }

    val isValid: Boolean
        get() {
            if (this.isToRemove || this.cardId !== CBuilderConst.NO_ID) {
                return true
            }

            // check question emptiness
            val checkStrings =
                mQuestion.split(CBuilderConst.LINE_BREAK.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (str in checkStrings) {
                if (str.trim { it <= ' ' }.length > 0) {
                    return true
                }
            }
            return false
        }

    fun hasId(): Boolean {
        return this.cardId !== CBuilderConst.NO_ID
    }

    fun hasQPackId(): Boolean {
        return this.qPackId !== CBuilderConst.NO_ID
    }
}
