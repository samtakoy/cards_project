package ru.samtakoy.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.core.database.CardsDbSchema;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.model.utils.MyStringUtils;

public class Card {

    private static final String IMAGES_DELIMITER = ",";

    public static Card createNew(Long qPackId, String question, String answer, String comment){
        return new Card().initNew(qPackId, question, answer, comment);
    }

    public static Card createEmpty(Long qPackId) {
        return new Card().initNew(qPackId, "", "", "");
    }

    private Long mId;
    private Long mQPackId;
    private String mQuestion;
    private String mAnswer;
    private List<String> mImages;
    private String mComment;
    private int mViews;
    private int mErrors;
    private int mLastGoodViews;
    private int mLastErrors;
    private Date mLastViewDate;


    private List<Tag> mTags;



    private Card initNew(Long qPackId, String question, String answer, String comment) {
        mId = 0L;
        mQPackId = qPackId;
        mQuestion = question;
        mAnswer = answer;
        mImages = new ArrayList<>();
        mComment = comment;
        mViews = 0;
        mErrors = 0;
        mLastGoodViews = 0;
        mLastErrors  = 0;

        mLastViewDate = new Date(0);


        createEmptyTags();
        return this;
    }

    private void createEmptyTags() {
        mTags = new ArrayList<>();
    }


    public void setId(Long id) {
        mId = id;
    }

    public void setQPackId(Long QPackId) {
        mQPackId = QPackId;
    }



    public static class CardCursor extends CursorWrapper {
        public CardCursor(Cursor cursor){ super(cursor); }
        public Card getCard(){
            Card result = new Card();
            result.mId = getLong(getColumnIndex(CardsDbSchema.Cards.Cols.ID));
            result.mQPackId = getLong(getColumnIndex(CardsDbSchema.Cards.Cols.QPACK_ID));
            result.mQuestion = getString(getColumnIndex(CardsDbSchema.Cards.Cols.QUESTION));
            result.mAnswer = getString(getColumnIndex(CardsDbSchema.Cards.Cols.ANSWER));

            result.mImages = parseImagesFromString(
                    getString(getColumnIndex(CardsDbSchema.Cards.Cols.AIMAGES))
            );

            result.mComment = getString(getColumnIndex(CardsDbSchema.Cards.Cols.COMMENT));
            result.mViews = getInt(getColumnIndex(CardsDbSchema.Cards.Cols.VIEWS));
            result.mErrors = getInt(getColumnIndex(CardsDbSchema.Cards.Cols.ERRORS));
            result.mLastGoodViews = getInt(getColumnIndex(CardsDbSchema.Cards.Cols.LAST_GOOD_VIEWS));
            result.mLastErrors = getInt(getColumnIndex(CardsDbSchema.Cards.Cols.LAST_ERRORS));
            result.mLastViewDate = DateUtils.getDateFromLong(getLong(getColumnIndex(CardsDbSchema.Cards.Cols.LAST_VIEW_DATE)));


            // TODO по хорошему вся инициадизация через 1 метод
            result.createEmptyTags();
            return result;
        }
    }

    public static class DummyIdCardCursor extends CursorWrapper {
        public DummyIdCardCursor(Cursor cursor){ super(cursor); }
        public Card getCard(){
            Card result = new Card();
            result.mId = getLong(getColumnIndex(CardsDbSchema.Cards.Cols.ID));
            result.mQPackId = getLong(getColumnIndex(CardsDbSchema.Cards.Cols.QPACK_ID));
            return result;
        }
    }


    public static class CardIdCursor extends CursorWrapper {
        public CardIdCursor(Cursor cursor){ super(cursor); }
        public Long getId(){
            return getLong(getColumnIndex(CardsDbSchema.Cards.Cols.ID));
        }
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues values = new ContentValues();
        if(withId){
            values.put(CardsDbSchema.Cards.Cols.ID, mId);
        }
        values.put(CardsDbSchema.Cards.Cols.QPACK_ID, mQPackId);
        values.put(CardsDbSchema.Cards.Cols.QUESTION, mQuestion);
        values.put(CardsDbSchema.Cards.Cols.ANSWER, mAnswer);
        values.put(CardsDbSchema.Cards.Cols.AIMAGES, serializeImages(mImages));
        values.put(CardsDbSchema.Cards.Cols.COMMENT, mComment);
        values.put(CardsDbSchema.Cards.Cols.VIEWS, mViews);
        values.put(CardsDbSchema.Cards.Cols.ERRORS, mErrors);
        values.put(CardsDbSchema.Cards.Cols.LAST_GOOD_VIEWS, mLastGoodViews);
        values.put(CardsDbSchema.Cards.Cols.LAST_ERRORS, mLastErrors);
        values.put(CardsDbSchema.Cards.Cols.LAST_VIEW_DATE, getLastViewDateAsLong());

        return values;
    }

    public Long getId() {
        return mId;
    }


    public Long getQPackId() {
        return mQPackId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public boolean hasQuestion() {
        return mQuestion != null && mQuestion.length()>0;
    }

    public boolean hasAnswer() {
        return mAnswer != null && mAnswer.length()>0;
    }

    private long getLastViewDateAsLong() {
        return mLastViewDate.getTime();
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public void addTag(Tag tag){
        mTags.add(tag);
    }

    public void addTagsFrom(List<Tag> tagList){
        for(Tag tag:tagList){ addTag(tag); }
    }

    private static List<String> parseImagesFromString(String serialized){

        if(serialized.length() == 0){
            return new ArrayList<>();
        }

        String[] parts = StringUtils.split(serialized, IMAGES_DELIMITER);
        List<String> result = new ArrayList<>(parts.length);
        for(String img:parts){ result.add(img); }
        return result;
    }

    private static String serializeImages(List<String> images){
        return MyStringUtils.join(images, IMAGES_DELIMITER);
    }

    public void setImages(List<String> images) {
        mImages = images;
    }

    public List<String> getImages() {
        return mImages;
    }



}

