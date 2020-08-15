package ru.samtakoy.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import ru.samtakoy.core.database.CardsDbSchema;

public class Tag {

    private Long mId;
    private String mTitle;
    private String mKey;

    public static Tag createNew(String title){
        return new Tag().initNew(title);
    }
    public static String titleToKey(String title){ return title.toLowerCase(); }

    private Tag initNew(String title) {
        mId = 0L;
        setTitle(title);
        return this;
    }

    private void setTitle(String title) {
        mTitle = title;
        mKey = titleToKey(mTitle);
    }

    public boolean hasId() {
        return mId>0;
    }

    public String getMapKey() {
        return mKey;
    }

    public static class TagCursor extends CursorWrapper {
        public TagCursor(Cursor cursor){ super(cursor); }
        public Tag getTag(){
            Tag result = new Tag();
            result.setId(getLong(getColumnIndex(CardsDbSchema.Tags.Cols.ID)));
            result.setTitle(getString(getColumnIndex(CardsDbSchema.Tags.Cols.TITLE)));
            return result;
        }
    }

    public Long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setId(Long id) {
        mId = id;
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues values = new ContentValues();
        if(withId){
            values.put(CardsDbSchema.Tags.Cols.ID, mId);
        }
        values.put(CardsDbSchema.Tags.Cols.TITLE, mTitle);
        return values;
    }

}
