package ru.samtakoy.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import ru.samtakoy.core.database.CardsDbSchema;

public class CardTag {

    private Long mCardId;
    private Long mTagId;

    public static CardTag createNew(Long cardId, Long tagId){
        return new CardTag().initNew(cardId, tagId);
    }

    private CardTag initNew(Long cardId, Long tagId) {
        mCardId = cardId;
        mTagId = tagId;
        return this;
    }

    public static class CardTagCursor extends CursorWrapper {
        public CardTagCursor(Cursor cursor){ super(cursor); }
        public CardTag getTag(){
            CardTag result = new CardTag();
            result.mCardId = getLong(getColumnIndex(CardsDbSchema.CardsTags.Cols.CARD_ID));
            result.mTagId = getLong(getColumnIndex(CardsDbSchema.CardsTags.Cols.TAG_ID));
            return result;
        }
    }

    public Long getCardId() {
        return mCardId;
    }

    public Long getTagId() {
        return mTagId;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(CardsDbSchema.CardsTags.Cols.CARD_ID, mCardId);
        values.put(CardsDbSchema.CardsTags.Cols.TAG_ID, mTagId);
        return values;
    }


}
