package ru.samtakoy.core.data.local.database.old;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// больше не используется (перешли на Room), оставил для истории
public class DbContentProvider extends ContentProvider {

    private static final String TAG = "DbContentProvider";

    private static final String AUTHORITY = "ru.samtakoy.core.data.local.database.old.DbContentProvider";

    /*
    public static final Uri CONTENT_URI_TODOS =
            Uri.parse("content://" + AUTHORITY + "/"+ CardsDbSchema.Todos.NAME);
    */

    private static final Uri CONTENT_URI_COURSES =
            Uri.parse("content://" + AUTHORITY + "/" + CardsDbSchema.LearnCourse.NAME);

    private static final Uri CONTENT_URI_THEMES =
            Uri.parse("content://" + AUTHORITY + "/" + CardsDbSchema.Themes.NAME);

    private static final Uri CONTENT_URI_QPACKS =
            Uri.parse("content://" + AUTHORITY + "/" + CardsDbSchema.QPacks.NAME);

    private static final Uri CONTENT_URI_CARDS =
            Uri.parse("content://" + AUTHORITY + "/" + CardsDbSchema.Cards.NAME);

    private static final Uri CONTENT_URI_TAGS =
            Uri.parse("content://" + AUTHORITY + "/" + CardsDbSchema.Tags.NAME);

    private static final Uri CONTENT_URI_CARD_TAGS =
            Uri.parse("content://" + AUTHORITY + "/" + CardsDbSchema.CardsTags.NAME);


    private MyDb mDb;
    private SQLiteDatabase mReadableDb;

    private ReadWriteLock mLock;

    @Override
    public boolean onCreate() {

        mLock = new ReentrantReadWriteLock();

        mLock.writeLock().lock();

        try {
            mDb = new MyDb(getContext());
            mReadableDb = null;
        } finally {
            mLock.writeLock().unlock();
        }
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

        Cursor cursor = null;

        mLock.readLock().lock();

        try {
            String table = getTableName(uri);
            SQLiteDatabase db = getReadableDb();
            cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        } finally {
            mLock.readLock().unlock();
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        long resultValue = 0;

        mLock.writeLock().lock();

        try {
            closeReadableDb();
            String table = getTableName(uri);
            SQLiteDatabase db = mDb.getWritableDatabase();
            resultValue = db.insert(table, null, contentValues);
            db.close();
        } finally {
            mLock.writeLock().unlock();
        }


        //db.
        return Uri.withAppendedPath(uri, String.valueOf(resultValue));
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        if (!uri.equals(CONTENT_URI_CARDS)) {
            return super.bulkInsert(uri, values);
        }

        int numInserted = 0;

        mLock.writeLock().lock();

        try {
            closeReadableDb();
            String table = getTableName(uri);
            SQLiteDatabase db = mDb.getWritableDatabase();


            db.beginTransaction();
            try {
                for (ContentValues oneValues : values) {
                    if (db.insertOrThrow(table, null, oneValues) <= 0) {
                        throw new SQLException("Failed to insert row into " + uri);
                    }
                }
                db.setTransactionSuccessful();
                numInserted = values.length;
                getContext().getContentResolver().notifyChange(uri, null);
            } finally {
                db.endTransaction();
                db.close();
            }
        } finally {
            mLock.writeLock().unlock();
        }

        return numInserted;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {

        int cnt = 0;

        mLock.writeLock().lock();

        try {
            closeReadableDb();
            String table = getTableName(uri);
            SQLiteDatabase db = mDb.getWritableDatabase();
            cnt = db.delete(table, whereClause, whereArgs);
            db.close();
        } finally {
            mLock.writeLock().unlock();
        }

        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        long resultValue = 0;

        mLock.writeLock().lock();

        try {
            closeReadableDb();
            String table = getTableName(uri);
            SQLiteDatabase db = mDb.getWritableDatabase();
            resultValue = db.update(table, contentValues, selection, selectionArgs);
            db.close();
        } finally {
            mLock.writeLock().unlock();
        }
        return (int) resultValue;
    }

    private String getTableName(Uri uri){
        List<String> path = uri.getPathSegments();
        return path.get(path.size()-1);
    }


}
