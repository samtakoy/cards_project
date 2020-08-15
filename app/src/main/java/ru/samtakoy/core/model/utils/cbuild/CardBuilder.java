package ru.samtakoy.core.model.utils.cbuild;

import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.Tag;

public class CardBuilder {

    private Long mQPackId;
    private Long mId;
    private String mQuestion;
    private String mAnswer;
    private List<String> mImages;
    private List<Tag> mTags;

    private boolean mToRemove;

    public CardBuilder(){

        mQPackId = CBuilderConst.NO_ID;
        mId = CBuilderConst.NO_ID;
        mQuestion = "";
        mAnswer = "";

        mImages = new ArrayList<>();
        mTags = new ArrayList<>();


        mToRemove = false;
    }

    public Card build(){
        Card result = Card.createNew(
                mQPackId,
                mQuestion,
                mAnswer,
                ""
        );
        if(hasId()){
            result.setId(getId());
        }
        result.setImages(mImages);
        result.addTagsFrom(mTags);
        return  result;
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

    public void addTag(Tag tag) {
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

        if(mToRemove || mId != CBuilderConst.NO_ID){
            return true;
        }

        // check question emptiness
        String[] checkStrings = mQuestion.split(CBuilderConst.LINE_BREAK);
        for(String str:checkStrings){
            if(str.trim().length() > 0){
                return true;
            }
        }
        return false;
    }

    public void setId(Long cardId) {
        mId = cardId;

    }

    public Long getId() { return mId; }
    public Long getQPackId() { return mQPackId; }

    public boolean hasId() {
        return mId != CBuilderConst.NO_ID;
    }
    public boolean hasQPackId() {
        return mQPackId != CBuilderConst.NO_ID;
    }
}
