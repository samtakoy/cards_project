package ru.samtakoy.features.import_export.utils.cbuild;

import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.core.database.room.entities.TagEntity;
import ru.samtakoy.core.database.room.entities.other.CardWithTags;

public class CardBuilder {

    private Long mQPackId;
    private Long mCardId;
    private String mQuestion;
    private String mAnswer;
    private List<String> mImages;
    private List<TagEntity> mTags;

    private boolean mToRemove;

    public CardBuilder(){

        mQPackId = CBuilderConst.NO_ID;
        mCardId = CBuilderConst.NO_ID;
        mQuestion = "";
        mAnswer = "";

        mImages = new ArrayList<>();
        mTags = new ArrayList<>();


        mToRemove = false;
    }

    public CardWithTags build() {
        CardWithTags result = CardWithTags.Companion.initNew(
                mQPackId, mQuestion, mAnswer, ""
        );
        if (hasId()) {
            result.getCard().setId(getCardId());
        }
        result.getCard().setAImages(mImages);
        result.addTagsFrom(mTags);
        return result;
    }

    public void setQPackId(Long qPackId) {
        mQPackId = qPackId;
    }

    public void setQuestion(String text) {
        mQuestion = text;
    }

    public void setAnswer(String text) {
        mAnswer = text;
    }

    public void addTag(TagEntity tag) {
        mTags.add(tag);
    }

    public void addImage(String imageName){
        mImages.add(imageName);
    }

    /*public boolean isEmpty(){
        return mQuestion.length()==0 && mAnswer.length()==0;
    }/**/

    public void toRemove(){
        mToRemove = true;
    }

    public boolean isToRemove(){
        return mToRemove;
    }

    public boolean isValid() {

        if (mToRemove || mCardId != CBuilderConst.NO_ID) {
            return true;
        }

        // check question emptiness
        String[] checkStrings = mQuestion.split(CBuilderConst.LINE_BREAK);
        for (String str : checkStrings) {
            if (str.trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    public void setCardId(Long cardId) {
        mCardId = cardId;

    }

    public Long getCardId() {
        return mCardId;
    }

    public Long getQPackId() {
        return mQPackId;
    }

    public boolean hasId() {
        return mCardId != CBuilderConst.NO_ID;
    }

    public boolean hasQPackId() {
        return mQPackId != CBuilderConst.NO_ID;
    }
}
