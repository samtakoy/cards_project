package ru.samtakoy.features.import_export.utils.cbuild;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.samtakoy.core.data.local.database.room.entities.TagEntity;


public class CardsParser {


    private List<CardBuilder> mCardBuilders;
    private OneCardParser mCurCardParser;
    private Long mQPackId;
    private boolean mNullifyId;

    public CardsParser(HashMap<String, TagEntity> tagMap, boolean nullifyId) {

        mCardBuilders = new LinkedList<>();
        mCurCardParser = new OneCardParser(tagMap);
        mNullifyId = nullifyId;
    }


    public void onStart(Long qPackId) {
        mQPackId = qPackId;
    }

    public void onFinalize(Long qPackId) {

        tryBuildCard();

        for(CardBuilder oneCard: getCardBuilders()){
            oneCard.setQPackId(qPackId);
        }
    }

    public void processLine(String line, String lowerLine) {

        if(lowerLine.startsWith(CBuilderConst.QUESTION_PREFIX)){
            tryBuildCard();
            mCurCardParser.openQuestion();

            String regexPattern = "^q:\\[(\\d+)\\](.*)$";
            Pattern r = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(line);


            if(m.find() && m.groupCount() == 2){
                // update card
                Long cardId = Long.parseLong(m.group(1));
                line = m.group(2);

                // TODO сначала сравнить по длинне или по символу [
                String trimmedLowerLine = line.toLowerCase().trim();
                if(trimmedLowerLine.equals(CBuilderConst.CARD_REMOVE_TAG) || trimmedLowerLine.equals(CBuilderConst.CARD_REMOVE_TAG2)){
                    // remove card
                    mCurCardParser.markCardToRemove(cardId);
                }else{
                    // update card
                    mCurCardParser.addLine(line, true);
                }


                mCurCardParser.setCardId(mNullifyId ? CBuilderConst.NO_ID : cardId);
            } else {
                mCurCardParser.addLine(line.substring(CBuilderConst.QUESTION_PREFIX.length()), true);
            }


        }else
        if(mCurCardParser.isCardOpened()) {
            if (lowerLine.startsWith(CBuilderConst.ANSWER_PREFIX)) {
                mCurCardParser.openAnswer();
                mCurCardParser.addLine(line.substring(CBuilderConst.ANSWER_PREFIX.length()), true);
            } else if (lowerLine.startsWith(CBuilderConst.TAGS_PREFIX)) {
                mCurCardParser.addTagsLine(line);
            } else if (lowerLine.startsWith(CBuilderConst.IMAGE_PREFIX)) {
                mCurCardParser.addImage(line.substring(CBuilderConst.IMAGE_PREFIX.length()));
            } else {
                mCurCardParser.addLine(line, false);
            }
        }
    }

    private void tryBuildCard() {
        if(mCurCardParser.isCardOpened() && mCurCardParser.isValidCard()){
            mCardBuilders.add(mCurCardParser.getBuilder(mQPackId));
        }else {
            mCurCardParser.reset();
        }
    }

    public boolean isEmpty(){
        return mCardBuilders.size() == 0;
    }

    public List<CardBuilder> getCardBuilders() {
        return mCardBuilders;
    }

}
