package ru.samtakoy.core.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.List;

public class DbContentProvider extends ContentProvider {

    private static final String TAG = "DbContentProvider";

    public static final String AUTHORITY = "ru.samtakoy.core.database.DbContentProvider";

    public static final Uri CONTENT_URI_TODOS =
            Uri.parse("content://" + AUTHORITY + "/"+ CardsDbSchema.Todos.NAME);

    public static final Uri CONTENT_URI_COURSES =
            Uri.parse("content://" + AUTHORITY + "/"+ CardsDbSchema.LearnCourse.NAME);

    public static final Uri CONTENT_URI_THEMES =
            Uri.parse("content://" + AUTHORITY + "/"+CardsDbSchema.Themes.NAME);

    public static final Uri CONTENT_URI_QPACKS =
            Uri.parse("content://" + AUTHORITY + "/"+CardsDbSchema.QPacks.NAME);

    public static final Uri CONTENT_URI_CARDS =
            Uri.parse("content://" + AUTHORITY + "/"+CardsDbSchema.Cards.NAME);

    public static final Uri CONTENT_URI_TAGS =
            Uri.parse("content://" + AUTHORITY + "/"+CardsDbSchema.Tags.NAME);

    public static final Uri CONTENT_URI_CARD_TAGS =
            Uri.parse("content://" + AUTHORITY + "/"+CardsDbSchema.CardsTags.NAME);


    private MyDb mDb;
    private SQLiteDatabase mReadableDb;

    @Override
    public boolean onCreate() {
        mDb = new MyDb(getContext());
        mReadableDb = null;
        return true;
    }

    private SQLiteDatabase getReadableDb(){
        if(mReadableDb == null){
            mReadableDb = mDb.getReadableDatabase();
        }
        return mReadableDb;
    }

    private void closeReadableDb(){
        if(mReadableDb != null){
            mReadableDb.close();
            mReadableDb = null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        closeReadableDb();
        String table = getTableName(uri);
        SQLiteDatabase db = mDb.getWritableDatabase();
        long value = db.insert(table, null, contentValues);
        db.close();
        //db.
        return Uri.withAppendedPath(uri, String.valueOf(value));
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        if(!uri.equals(CONTENT_URI_CARDS)) {
            return super.bulkInsert(uri, values);
        }

        closeReadableDb();
        String table = getTableName(uri);
        SQLiteDatabase db = mDb.getWritableDatabase();
        int numInserted = 0;

        db.beginTransaction();
        try{
            for(ContentValues oneValues:values){
                if(db.insertOrThrow(table, null, oneValues)<=0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            db.setTransactionSuccessful();
            numInserted = values.length;
            getContext().getContentResolver().notifyChange(uri, null);
        }finally {
            db.endTransaction();
            db.close();
        }
        return numInserted;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        closeReadableDb();
        String table = getTableName(uri);
        SQLiteDatabase db = mDb.getWritableDatabase();
        int cnt = db.delete(table, whereClause, whereArgs);
        db.close();
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        closeReadableDb();
        String table = getTableName(uri);
        SQLiteDatabase db = mDb.getWritableDatabase();
        long value = db.update(table, contentValues, selection, selectionArgs);
        db.close();
        return (int)value;
    }

    private String getTableName(Uri uri){
        List<String> path = uri.getPathSegments();
        return path.get(path.size()-1);
    }



}
