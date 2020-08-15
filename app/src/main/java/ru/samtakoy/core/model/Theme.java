package ru.samtakoy.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.LinkedList;
import java.util.List;

import ru.samtakoy.core.database.CardsDbSchema;

public class Theme {

    public static Theme createNew(String title, Long parentId){
        return new Theme().initNew(title, parentId);
    }

    private Long mId;
    private String mTitle;
    private Long mParentId;

    private List<Theme> mChilds;

    public Theme(){

        mChilds = new LinkedList<>();
    }

    private Theme initNew(String title, Long parentId) {
        mId = 0l;
        mTitle = title;
        mParentId = parentId;
        return this;
    }

    public static class ThemeCursor extends CursorWrapper{
        public ThemeCursor(Cursor cursor){ super(cursor); }
        public Theme getTheme(){
            Theme result = new Theme();
            result.mId = getLong(getColumnIndex(CardsDbSchema.Themes.Cols.ID));
            result.mTitle = getString(getColumnIndex(CardsDbSchema.Themes.Cols.TITLE));
            result.mParentId = getLong(getColumnIndex(CardsDbSchema.Themes.Cols.PARENT));
            return result;
        }
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues values = new ContentValues();
        if(withId){
            values.put(CardsDbSchema.Themes.Cols.ID, mId);
        }
        values.put(CardsDbSchema.Themes.Cols.TITLE, mTitle);
        values.put(CardsDbSchema.Themes.Cols.PARENT, mParentId);
        return values;
    }

    public Long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getParentId() {
        return mParentId;
    }

    public String toString(){
        return "{id:"+mId+", parent:"+mParentId+", t:"+mTitle+"}";
    }
}
