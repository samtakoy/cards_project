package ru.samtakoy.core.model.utils.cbuild;

import java.util.HashMap;

import ru.samtakoy.core.model.Tag;

public class OneCardParser {




    enum BuildPhase{NONE, QUESTION, ANSWER, FINISHED};

    private HashMap<String, Tag> mTagMap;

    private StringBuilder mText;
    private CardBuilder mCurCardBuilder;

    private boolean mIsTextClosed;

    public OneCardParser(HashMap<String, Tag>tagMap) {
        mTagMap = tagMap;
        mText = new StringBuilder();
        mIsTextClosed = false;
    }

    public void reset(){

        mCurCardBuilder = null;
        mText.setLength(0);
        mIsTextClosed = false;
    }

    public void openQuestion(){
        reset();
        mCurCardBuilder = new CardBuilder();
    }

    public void openAnswer(){

        mCurCardBuilder.setQuestion(mText.toString());
        mText.setLength(0);
    }

    public void addTagsLine(String tagsLine){
        if(tagsLine.trim().length()>1){
            String[] tags = tagsLine.split("#");
            for(String tagTitle:tags){
                // первая будет пустая
                if(tagTitle.length() > 0){
                    addTag(tagTitle.trim());
                }
            }
        }

        mIsTextClosed = true;
    }

    private void addTag(String tagTitle){
        String key = Tag.titleToKey(tagTitle);
        Tag tag = mTagMap.get(key);
        if(tag == null){
            tag = Tag.createNew(tagTitle);
            mTagMap.put(tag.getMapKey(), tag);
        }
        mCurCardBuilder.addTag(tag);
    }

    public void addImage(String imageLine) {

        imageLine = imageLine.trim();
        if(imageLine.length()>1){
            String[] images = imageLine.split(",");
            for(String img:images){
                img = img.trim();
                if(img.length() > 0){
                    mCurCardBuilder.addImage(img);
                }
            }
        }
    }

    public void addLine(String line, boolean isFirstLine){
        if(!mIsTextClosed) {
            if (!isFirstLine) {
                mText.append(CBuilderConst.LINE_BREAK);
            }
            mText.append(line);
        }
    }

    public CardBuilder getBuilder(Long qPackId){

        if(!isCardOpened()){
            return null;
        }

        // ?
        //mCurCardBuilder.setQPackId(qPackId);
        mCurCardBuilder.setAnswer(mText.toString());

        CardBuilder result = mCurCardBuilder;

        reset();
        return  result;
    }

    public boolean isCardOpened(){
        return  mCurCardBuilder != null;
    }

    public boolean isValidCard() {
        return  mCurCardBuilder != null && mCurCardBuilder.isValid();
    }

    public void markCardToRemove(Long cardId) {
        setCardId(cardId);
        mCurCardBuilder.toRemove();
    }

    public void setCardId(Long cardId) {
        mCurCardBuilder.setId(cardId);
    }

}
