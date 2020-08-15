package ru.samtakoy.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.samtakoy.core.database.CardsDbSchema;
import ru.samtakoy.core.model.utils.DateUtils;

public class QPack implements Serializable {

    //private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm:ss";
    public static final String DEF_DATE = "01-01-2000 00:00:00";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    public static QPack createNew(Long themeId, String path, String fileName, String title, String comment){
        return new QPack().initNew(themeId, path, fileName, title, comment);
    }

    private Long mId;
    private Long mThemeId;
    private String mPath;
    private String mFileName;
    private String mTitle;
    private String mDesc;
    private Date mCreationDate;
    //private java.sql.Date mLastViewDate;
    private int mViewCount;
    private Date mLastViewDate;

    private QPack initNew(Long themeId, String path, String fileName, String title, String desc) {
        mId = 0L;
        mThemeId = themeId;
        mPath = path;
        mFileName = fileName;
        mTitle = title;
        mDesc = desc;
        mCreationDate = new Date(DateUtils.getCurrentTimeLong());
        mViewCount = 0;
        mLastViewDate = new Date(DateUtils.getCurrentTimeLong());
        return this;
    }



    public static class QPackCursor extends CursorWrapper {
        public QPackCursor(Cursor cursor){ super(cursor); }
        public QPack getQPack(){
            QPack result = new QPack();
            result.mId =      getLong(getColumnIndex(CardsDbSchema.QPacks.Cols.ID));
            result.mThemeId = getLong(getColumnIndex(CardsDbSchema.QPacks.Cols.THEME_ID));
            result.mPath = getString(getColumnIndex(CardsDbSchema.QPacks.Cols.PATH));
            result.mFileName = getString(getColumnIndex(CardsDbSchema.QPacks.Cols.FILE_NAME));
            result.mTitle = getString(getColumnIndex(CardsDbSchema.QPacks.Cols.TITLE));
            result.mDesc = getString(getColumnIndex(CardsDbSchema.QPacks.Cols.DESC));
            result.mCreationDate = DateUtils.getDateFromLong(getLong(getColumnIndex(CardsDbSchema.QPacks.Cols.CREATION_DATE)));
            result.mViewCount = getInt(getColumnIndex(CardsDbSchema.QPacks.Cols.VIEW_COUNTER));
            result.mLastViewDate = DateUtils.getDateFromLong(getLong(getColumnIndex(CardsDbSchema.QPacks.Cols.LAST_VIEW_DATE)));
            return result;
        }
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues values = new ContentValues();
        if(withId){
            values.put(CardsDbSchema.QPacks.Cols.ID, mId);
        }
        values.put(CardsDbSchema.QPacks.Cols.THEME_ID, mThemeId);
        values.put(CardsDbSchema.QPacks.Cols.PATH, mPath);
        values.put(CardsDbSchema.QPacks.Cols.FILE_NAME, mFileName);
        values.put(CardsDbSchema.QPacks.Cols.TITLE, mTitle);
        values.put(CardsDbSchema.QPacks.Cols.DESC, mDesc);
        values.put(CardsDbSchema.QPacks.Cols.CREATION_DATE, getCreationDateAsLong());
        values.put(CardsDbSchema.QPacks.Cols.VIEW_COUNTER, mViewCount);
        values.put(CardsDbSchema.QPacks.Cols.LAST_VIEW_DATE, getLastViewDateAsLong());
        return values;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getTitle() { return mTitle; }
    public boolean hasTitle() { return mTitle != null && mTitle.length() > 0; }

    public Long getThemeId() { return mThemeId; }

    public String getDesc() { return mDesc; }
    public boolean hasDesc() { return mDesc != null && mDesc.length()>0; }

    public int getViewCount() { return mViewCount; }
    public void setViewCount(int viewCount) { mViewCount = viewCount; }

    public String getExportFileName() {
        String result = mFileName.length() > 0 ? mFileName : String.valueOf(mId);
        return (result.indexOf('.') < 0) ? result + ".txt" : result;
    }

    public long getCreationDateAsLong() { return mCreationDate.getTime(); }

    public long getLastViewDateAsLong() {
        return mLastViewDate.getTime();
    }

    public String getLastViewDateAsString() {
        return DATE_FORMAT.format(mLastViewDate);
    }

    public void setLastViewDateMillis(long timeMillis){
        setLastViewDate(new Date(timeMillis));
    }
    public void setLastViewDate(Date date){
        mLastViewDate = date;
    }

    public String getCreationDateAsString() {
        return DATE_FORMAT.format(mCreationDate);
    }
    public boolean parseCreationDateFromString(String src) {
        try {
            mCreationDate = DATE_FORMAT.parse(src);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
