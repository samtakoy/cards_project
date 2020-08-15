package ru.samtakoy.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.samtakoy.core.database.CardsDbSchema.*;

public class MyDb extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static final String DB_NAME = "cards.db";

    public MyDb(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(
                "CREATE TABLE "+Themes.NAME + "("
                + Themes.Cols.ID + " integer primary key autoincrement, "
                + Themes.Cols.TITLE + " text, "
                + Themes.Cols.PARENT + " integer "
                +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+Tags.NAME + "("
                        + Tags.Cols.ID + " integer primary key autoincrement, "
                        + Tags.Cols.TITLE + " text "
                        +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+ThemesTags.NAME + "("
                        + ThemesTags.Cols.THEME_ID + " integer, "
                        + ThemesTags.Cols.TAG_ID + " integer "
                        +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+QPacks.NAME + "("
                        + QPacks.Cols.ID + " integer primary key autoincrement, "
                        + QPacks.Cols.THEME_ID  + " integer, "
                        + QPacks.Cols.PATH      + " text, "
                        + QPacks.Cols.TITLE     + " text NOT NULL DEFAULT '', "
                        + QPacks.Cols.FILE_NAME + " text NOT NULL DEFAULT '', "
                        + QPacks.Cols.DESC      + " text NOT NULL DEFAULT '', "
                        + QPacks.Cols.CREATION_DATE + " integer, "
                        + QPacks.Cols.VIEW_COUNTER + " integer NOT NULL DEFAULT 0, "
                        + QPacks.Cols.LAST_VIEW_DATE + " integer "
                        +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+Cards.NAME + "("
                        + Cards.Cols.ID + " integer primary key autoincrement, "
                        + Cards.Cols.QPACK_ID  + " integer, "
                        + Cards.Cols.QUESTION  + " text, "
                        + Cards.Cols.ANSWER    + " text, "
                        + Cards.Cols.AIMAGES   + " text, "
                        + Cards.Cols.COMMENT   + " text, "
                        + Cards.Cols.VIEWS     + " integer, "
                        + Cards.Cols.ERRORS    + " integer, "
                        + Cards.Cols.LAST_GOOD_VIEWS + " integer, "
                        + Cards.Cols.LAST_ERRORS + " integer, "
                        + Cards.Cols.LAST_VIEW_DATE + " integer "

                        +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+CardsTags.NAME + "("
                        + CardsTags.Cols.CARD_ID + " integer, "
                        + CardsTags.Cols.TAG_ID + " integer "
                        +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+Todos.NAME + "("
                        + Todos.Cols.ID + " integer primary key autoincrement, "
                        + Todos.Cols.THEME_ID + " integer, "
                        + Todos.Cols.CARD_ID + " integer, "
                        + Todos.Cols.TEXT + " text "
                        +")"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE "+ LearnCourse.NAME + "("

                        + LearnCourse.Cols.ID + " integer primary key autoincrement, "
                        + LearnCourse.Cols.QPACK_ID + " integer, "
                        + LearnCourse.Cols.COURSE_TYPE+ " integer, "
                        + LearnCourse.Cols.PRIMARY_COURSE_ID + " integer, "

                        + LearnCourse.Cols.TITLE + " text, "
                        + LearnCourse.Cols.MODE + " integer, "
                        + LearnCourse.Cols.REPEATED_COUNT + " integer, "

                        + LearnCourse.Cols.CARD_IDS + " text, "
                        + LearnCourse.Cols.TODO_CARD_IDS + " text, "
                        + LearnCourse.Cols.ERROR_CARD_IDS + " text, "

                        + LearnCourse.Cols.REST_SCHEDULE + " text, "
                        + LearnCourse.Cols.REALIZED_SCHEDULE + " text, "
                        + LearnCourse.Cols.REPEAT_DATE + " integer "

                        +")"
        );

        /*
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+RepeatPlan.NAME + "("
                        + RepeatPlan.Cols.ID + " integer primary key autoincrement, "
                        + RepeatPlan.Cols.CARD_IDS + " text, "
                        + RepeatPlan.Cols.TODO_CARD_IDS + " text, "
                        + RepeatPlan.Cols.ERROR_CARD_IDS + " text "
                        +")"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+ RepeatShedules.NAME + "("
                        + RepeatShedules.Cols.PLAN_ID + " integer, "
                        + RepeatShedules.Cols.REPEAT_DATE + " integer "
                        +")"
        );/***/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
