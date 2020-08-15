package ru.samtakoy.core.business.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.database.CardsDbSchema;
import ru.samtakoy.core.database.DbContentProvider;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.CardTag;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Tag;
import ru.samtakoy.core.model.Theme;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.screens.log.MyLog;

import static ru.samtakoy.core.model.LearnCourseMode.LEARN_WAITING;
import static ru.samtakoy.core.model.LearnCourseMode.REPEAT_WAITING;

class ContentProviderHelper {

    private static final String TAG = "ContentProviderHelper";

    public static Long addNewCourse(Context ctx, LearnCourse learnCourse) {
        ContentResolver resolver = ctx.getContentResolver();
        Uri courseUri = resolver.insert(DbContentProvider.CONTENT_URI_COURSES, learnCourse.getContentValues(false));
        Long courseId = Long.parseLong(courseUri.getLastPathSegment());

MyLog.add("NEW COURSE ID:"+courseId);

        learnCourse.setId(courseId);
        return courseId;
    }

    public static boolean deleteCourse(Context ctx, Long learnCourseId){
        ContentResolver resolver = ctx.getContentResolver();
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_COURSES,
                CardsDbSchema.LearnCourse.Cols.ID+" = ?",
                new String[]{String.valueOf(learnCourseId)}
        );
        return cnt  > 0;
    }

    public static int deleteCoursesByPackId(Context ctx, Long qPackId){
        ContentResolver resolver = ctx.getContentResolver();
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_COURSES,
                CardsDbSchema.LearnCourse.Cols.QPACK_ID+" = ?",
                new String[]{String.valueOf(qPackId)}
        );

        MyLog.add("courses deleted:"+cnt);

        return cnt;
    }

    public static int deleteTodoByCardId(Context ctx, Long cardId){
        ContentResolver resolver = ctx.getContentResolver();
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_TODOS,
                CardsDbSchema.Todos.Cols.CARD_ID+" = ?",
                new String[]{String.valueOf(cardId)}
        );
        return cnt;
    }

    public static List<Tag> getCardTags(ContentResolver resolver, Long cardId){

        List<Tag> result = new ArrayList<>();
        List<CardTag> cardTags = getCardTagPairs(resolver, cardId);

        for(CardTag cardTag:cardTags){
            result.add(getTag(resolver, cardTag.getTagId()));
        }
        return result;
    }

    //public static int tagsToCardTags

    public static int addCardTags(ContentResolver resolver, Long cardId, List<Tag> tags){

        if(tags.size() == 0){
            return 0;
        }

        ContentValues[] contentValues = new ContentValues[tags.size()];

        for(int i=0; i<tags.size(); i++){

            CardTag ct = CardTag.createNew(cardId, tags.get(i).getId());
            contentValues[i] = ct.getContentValues();
        }

        return resolver.bulkInsert(DbContentProvider.CONTENT_URI_CARD_TAGS, contentValues);
    }

    public static Tag getTag(ContentResolver resolver, Long tagId) {
        Tag.TagCursor c = new Tag.TagCursor(
                resolver.query(DbContentProvider.CONTENT_URI_TAGS,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.Tags.Cols.ID+" = ?",         // selection
                        new String[] { String.valueOf(tagId) },              // selectionArgs
                        null                          // sortOrder
                )
        );
        return returnFirstTagFromCursor(c);
    }

    @Nullable
    private static Tag returnFirstTagFromCursor(Tag.TagCursor c) {
        Tag result = null;
        try {
            c.moveToFirst();
            if(!c.isAfterLast()) {
                result = c.getTag();
            }
        }catch (Exception e){
            // do nothing
            Log.e(TAG, "returnFirstCourseFromCursor", e);
        }finally {
            c.close();
        }
        return result;
    }

    public static List<CardTag> getCardTagPairs(ContentResolver resolver, Long cardId){

        // TODO; составной запрос - сразу загрузить теги карты

        List<CardTag> result = new ArrayList<>();

        CardTag.CardTagCursor c = new CardTag.CardTagCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_CARD_TAGS,
                        null,//new String[] { "_id", "description" } , // projection
                        CardsDbSchema.CardsTags.Cols.CARD_ID+" = ?",// selection
                        new String[]{String.valueOf(cardId)},// selectionArgs
                        null// sortOrder
                ));

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                result.add(c.getTag());
                c.moveToNext();
            }
        }finally {
            c.close();
        }

        return result;
    }

    public static int deleteCardTagsByCardId(ContentResolver resolver, Long cardId){

        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_CARD_TAGS,
                CardsDbSchema.CardsTags.Cols.CARD_ID+" = ?",
                new String[]{String.valueOf(cardId)}
        );
        return cnt;
    }

    public static Card getDummyIdCard(ContentResolver resolver, Long cardId) {
        Card.DummyIdCardCursor c = new Card.DummyIdCardCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_CARDS,
                        new String[] {CardsDbSchema.Cards.Cols.ID, CardsDbSchema.Cards.Cols.QPACK_ID}, // projection
                        CardsDbSchema.Cards.Cols.ID+" = ?",         // selection
                        new String[] { String.valueOf(cardId) },              // selectionArgs
                        null                          // sortOrder
                ));

        try {
            c.moveToFirst();
            if(!c.isAfterLast()){ return c.getCard(); }
        }finally {
            c.close();
        }
        return  null;
    }


    public static Card getConcreteCard(ContentResolver resolver, Long cardId) {
        Card.CardCursor c = new Card.CardCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_CARDS,
                        null , // projection
                        CardsDbSchema.Cards.Cols.ID+" = ?",         // selection
                        new String[] { String.valueOf(cardId) },              // selectionArgs
                        null                          // sortOrder
                ));

        try {
            c.moveToFirst();
            if(!c.isAfterLast()){ return c.getCard(); }
        }finally {
            c.close();
        }
        return  null;
    }

    /**
     *
     * При удалении карточки - она останется в курсе
     * при чтении карточек в просмоторщике - учитывать этот момент
     *
     * */
    public static int deleteCard(ContentResolver resolver, Long cardId){
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_CARDS,
                CardsDbSchema.Cards.Cols.ID+" = ?",
                new String[]{String.valueOf(cardId)}
        );
        return cnt;
    }

    public static int deleteCardsByQPackID(Context ctx, Long qPackId){
        ContentResolver resolver = ctx.getContentResolver();
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_CARDS,
                CardsDbSchema.Cards.Cols.QPACK_ID+" = ?",
                new String[]{String.valueOf(qPackId)}
        );
        return cnt;
    }

    public static boolean saveCard(ContentResolver resolver, Card card) {
        return resolver.update(

                DbContentProvider.CONTENT_URI_CARDS,
                card.getContentValues(true),
                CardsDbSchema.Cards.Cols.ID + " = ?",
                new String[]{String.valueOf(card.getId())}) > 0;
    }

    public static boolean saveCourse(Context ctx, LearnCourse learnCourse) {

        MyLog.add("saveCourse, id:"+learnCourse.getId()+", nextRD:"+ learnCourse.getRepeatDateDebug());
        ContentResolver resolver = ctx.getContentResolver();
        return  resolver.update(
                DbContentProvider.CONTENT_URI_COURSES,
                learnCourse.getContentValues(true),
                CardsDbSchema.LearnCourse.Cols.ID+" = ?",
                new String[]{String.valueOf(learnCourse.getId())}) > 0;
    }




    public static LearnCourse getConcreteCourse(Context ctx, Long courseId) {
        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.LearnCourse.Cols.ID+" = ?",         // selection
                        new String[] { String.valueOf(courseId) },              // selectionArgs
                        null                          // sortOrder
                )
        );
        return returnFirstCourseFromCursor(c);
    }

    public static LearnCourse getTempCourse(Context ctx) {
        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.LearnCourse.Cols.MODE+" = ?",         // selection
                        new String[] { String.valueOf(LearnCourseMode.TEMPORARY.getId()) },              // selectionArgs
                        null                          // sortOrder
                )
        );
        return returnFirstCourseFromCursor(c);
    }

    @Nullable
    private static LearnCourse returnFirstCourseFromCursor(LearnCourse.LearnPlanCursor c) {
        LearnCourse result = null;
        try {
            c.moveToFirst();
            if(!c.isAfterLast()) {
                result = c.getLearnCourse();
            }
        }catch (Exception e){
            // do nothing
            Log.e(TAG, "returnFirstCourseFromCursor", e);
        }finally {
            c.close();
        }
        return result;
    }

    @NotNull
    private static List<LearnCourse> returnCoursesFromCursor(LearnCourse.LearnPlanCursor c) {

        List<LearnCourse> result = null;
        try {
            result = new ArrayList<>(c.getCount());
            c.moveToFirst();
            while (!c.isAfterLast()) {
                result.add(c.getLearnCourse());
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.e(TAG, "returnCoursesFromCursor", e);
            result = new ArrayList<>();
        } finally {
            c.close();
        }

        return result;
    }

    public static List<LearnCourse> getCoursesFor(Context ctx, Long qPackId) {

        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.LearnCourse.Cols.QPACK_ID+" = ?",         // selection
                        new String[] { String.valueOf(qPackId) },              // selectionArgs
                        null                          // sortOrder
                )
        );

        return returnCoursesFromCursor(c);
    }



    public static List<LearnCourse> getCoursesByModes(Context ctx, List<LearnCourseMode> modes) {

        if(modes.size() == 0){ return new ArrayList<LearnCourse>(); }

        String[] selectionArgs = new String[modes.size()];

        String pattern = CardsDbSchema.LearnCourse.Cols.MODE + " = ? ";
        StringBuilder selectionBuilder = new StringBuilder();
        selectionBuilder.append(pattern);
        selectionArgs[0] = String.valueOf(modes.get(0).getId());
        for(int i=1; i<modes.size(); i++){
            selectionBuilder.append(" OR ");
            selectionBuilder.append(pattern);
            selectionArgs[i] = String.valueOf(modes.get(i).getId());
        }

        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null,
                        selectionBuilder.toString(),
                        selectionArgs,
                        null
                )
        );

        return returnCoursesFromCursor(c);
    }

    public static List<LearnCourse> getCoursesByIds(Context ctx, Long[] courseIds) {

        if(courseIds.length == 0){ return new ArrayList<LearnCourse>(); }

        String[] selectionArgs = new String[courseIds.length];

        StringBuilder selectionBuilder = new StringBuilder();
        selectionBuilder.append(CardsDbSchema.LearnCourse.Cols.ID+" IN (?");
        selectionArgs[0] = String.valueOf(courseIds[0]);
        for(int i=1; i<courseIds.length; i++){
            selectionBuilder.append(",?");
            selectionArgs[i] = String.valueOf(courseIds[i]);
        }
        selectionBuilder.append(")");

        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null,
                        selectionBuilder.toString(),
                        selectionArgs,
                        null
                )
        );

        return returnCoursesFromCursor(c);
    }

    public static List<LearnCourse> getUncompletedCourses(Context ctx) {

        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null,
                        CardsDbSchema.LearnCourse.Cols.MODE+" = ? OR "+
                        CardsDbSchema.LearnCourse.Cols.MODE+" = ?",
                        new String[] { String.valueOf(LEARN_WAITING.getId()), String.valueOf(REPEAT_WAITING.getId()) },
                        null
                )
        );

        return returnCoursesFromCursor(c);
    }

    public static List<LearnCourse> getCoursesLessThan(Context ctx, LearnCourseMode mode, Date repeatDate) {
        return getCoursesWithComparator(ctx, mode, repeatDate, "<=");
    }

    public static List<LearnCourse> getCoursesMoreThan(Context ctx, LearnCourseMode mode, Date repeatDate) {
        return getCoursesWithComparator(ctx, mode, repeatDate, ">");
    }

    private static List<LearnCourse> getCoursesWithComparator(
            Context ctx, LearnCourseMode mode, Date repeatDate, String comparator
    ) {

        long intRepeatDate = DateUtils.dateToDbSerialized(repeatDate);
        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.LearnCourse.Cols.MODE+" = ? AND "
                                +CardsDbSchema.LearnCourse.Cols.REPEAT_DATE+" "+comparator+" ? ",         // selection
                        new String[] { String.valueOf(mode.getId()), String.valueOf(intRepeatDate) },              // selectionArgs
                        CardsDbSchema.LearnCourse.Cols.REPEAT_DATE + " ASC"                          // sortOrder
                )
        );

        return returnCoursesFromCursor(c);
    }

    public static List<LearnCourse> getAllCourses(Context ctx) {
        ContentResolver resolver = ctx.getContentResolver();
        LearnCourse.LearnPlanCursor c = new LearnCourse.LearnPlanCursor(
                resolver.query(DbContentProvider.CONTENT_URI_COURSES,
                        null, //new String[] { "_id", "description" } , // projection
                        null,         // selection
                        null,              // selectionArgs
                        null                          // sortOrder
                )
        );
        return returnCoursesFromCursor(c);
    }

    public static List<QPack> getCurQPacks(Context ctx, Long parentThemeId) {

        ContentResolver resolver = ctx.getContentResolver();
        QPack.QPackCursor c = new QPack.QPackCursor(
                resolver.query(DbContentProvider.CONTENT_URI_QPACKS,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.QPacks.Cols.THEME_ID+" = ?",         // selection
                        new String[] { String.valueOf(parentThemeId) },              // selectionArgs
                        null                          // sortOrder
                )
        );
        return qPacksListFromCursor(c);
    }

    public static List<QPack> getAllQPacks(Context ctx) {

        ContentResolver resolver = ctx.getContentResolver();
        QPack.QPackCursor c = new QPack.QPackCursor(
                resolver.query(DbContentProvider.CONTENT_URI_QPACKS,
                        null, //new String[] { "_id", "description" } , // projection
                        null,
                        null,
                        null
                )
        );
        return qPacksListFromCursor(c);
    }


    @NotNull
    private static List<QPack> qPacksListFromCursor(QPack.QPackCursor c) {
        List<QPack> result = null;
        try {
            result = new ArrayList<>(c.getCount());
            c.moveToFirst();
            while (!c.isAfterLast()) {
                result.add(c.getQPack());
                c.moveToNext();
            }
        }catch (Exception e){
            Log.e(TAG, "getCurQPacks", e);
            result = new ArrayList<>();
        }finally {
            c.close();
        }
        return result;
    }

    public static boolean saveQPack(ContentResolver resolver, QPack qPack) {
        return resolver.update(
                DbContentProvider.CONTENT_URI_QPACKS,
                qPack.getContentValues(true),
                CardsDbSchema.QPacks.Cols.ID + " = ?",
                new String[]{String.valueOf(qPack.getId())}) > 0;
    }

    // TODO
    // удаление темы без проверки на пустотелость
    public static boolean deleteThemeOnlyUnchecked(Context ctx, Long themeId){
        ContentResolver resolver = ctx.getContentResolver();
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_THEMES,
                CardsDbSchema.Themes.Cols.ID+" = ?",
                new String[]{String.valueOf(themeId)}
        );
        return cnt  > 0;
    }

    public static Theme getTheme(Context ctx, Long themeId){
        ContentResolver resolver = ctx.getContentResolver();
        Theme.ThemeCursor c = new Theme.ThemeCursor(
                resolver.query(DbContentProvider.CONTENT_URI_THEMES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.Themes.Cols.ID+" = ?",         // selection
                        new String[] { String.valueOf(themeId) },              // selectionArgs
                        null                          // sortOrder
                )
        );
        return firstThemeFromCursor(c);
    }

    public static List<Theme> getCurThemes(Context ctx, Long parentThemeId) {

        ContentResolver resolver = ctx.getContentResolver();
        Theme.ThemeCursor c = new Theme.ThemeCursor(
                resolver.query(DbContentProvider.CONTENT_URI_THEMES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.Themes.Cols.PARENT+" = ?",         // selection
                        new String[] { String.valueOf(parentThemeId) },              // selectionArgs
                        null                          // sortOrder
                )
        );

        return themesListFromCursor(c);
    }



    public static List<Theme> getAllThemes(ContentResolver resolver) {
        Theme.ThemeCursor c = new Theme.ThemeCursor(
                resolver.query(DbContentProvider.CONTENT_URI_THEMES,
                        null, //new String[] { "_id", "description" } , // projection
                        null,         // selection
                        null,              // selectionArgs
                        null                          // sortOrder
                )
        );

        return themesListFromCursor(c);
    }

    public static Theme getThemeWithName(ContentResolver resolver, Long parentThemeId, String childName) {

        Theme.ThemeCursor c = new Theme.ThemeCursor(
                resolver.query(DbContentProvider.CONTENT_URI_THEMES,
                        null, //new String[] { "_id", "description" } , // projection
                        CardsDbSchema.Themes.Cols.PARENT+" = ? AND "
                        + CardsDbSchema.Themes.Cols.TITLE+" = ? ",
                        new String[] { String.valueOf(parentThemeId), childName },
                        null
                )
        );

        return firstThemeFromCursor(c);
    }

    @NotNull
    private static Theme firstThemeFromCursor(Theme.ThemeCursor c) {
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                return  c.getTheme();
            }
        } catch (Exception e) {
            MyLog.add("ContentProviderHelper::firstThemeFromCursor: "+e.toString());
        } finally {
            c.close();
        }
        return null;
    }

    @NotNull
    private static List<Theme> themesListFromCursor(Theme.ThemeCursor c) {
        List<Theme> result = null;
        try {
            result = new ArrayList<>(c.getCount());
            c.moveToFirst();
            while (!c.isAfterLast()) {
                result.add(c.getTheme());
                c.moveToNext();
            }
        } catch (Exception e) {
            result = new ArrayList<>();
        } finally {
            c.close();
        }
        return result;
    }

    public static List<Card> getQPackCards(Context ctx, Long qPackId){
        ContentResolver resolver = ctx.getContentResolver();
        Card.CardCursor c = new Card.CardCursor(
                resolver.query(DbContentProvider.CONTENT_URI_CARDS,
                        null, //new String[] { "_id", "description" } ,
                        CardsDbSchema.Cards.Cols.QPACK_ID+" = ?",
                        new String[] { String.valueOf(qPackId) },
                        null
                ));
        return cardsListFromCursor(ctx, c);
    }

    public static int getQPackCardCount(ContentResolver resolver, Long qPackId) {
        Card.CardCursor c = new Card.CardCursor(
                resolver.query(DbContentProvider.CONTENT_URI_CARDS,
                        null,
                        CardsDbSchema.Cards.Cols.QPACK_ID+" = ?",
                        new String[] { String.valueOf(qPackId) },
                        null
                ));
        try {
            return c.getCount();
        }finally {
            c.close();
        }
    }

    public static List<Card> getQPackCardsWithTags(Context ctx, Long qPackId){

        ContentResolver resolver = ctx.getContentResolver();
        List<Card> result = getQPackCards(ctx, qPackId);
        for(Card card:result){
            card.addTagsFrom(getCardTags(resolver, card.getId()));
        }
        return result;
    }

    @NotNull
    private static List<Card> cardsListFromCursor(Context ctx, Card.CardCursor c) {
        List<Card> result = new ArrayList<>();
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                result.add(c.getCard());
                c.moveToNext();
            }
        } catch (Exception e){
            Log.e(TAG, e.toString(), e);
            Toast.makeText(ctx, R.string.fragment_themes_list_cards_cards_loading_error, Toast.LENGTH_SHORT).show();
        } finally {
            c.close();
        }
        return result;
    }

    public static HashMap<String, Tag> buildTagMap(ContentResolver resolver){
        HashMap<String, Tag> tagHashMap = new HashMap<>();

        Tag.TagCursor c = new Tag.TagCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_TAGS,
                        null,//new String[] { "_id", "description" } , // projection
                        null,// selection
                        null,// selectionArgs
                        null// sortOrder
                ));

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                tagHashMap.put(c.getTag().getMapKey(), c.getTag());
                c.moveToNext();
            }
        }finally {
            c.close();
        }

        return tagHashMap;
    }


    public static List<Long> getQPackCardIdsAsList(ContentResolver resolver, Long qPackId) {
        Long[] arrayOfCardIds = ContentProviderHelper.getQPackCardIds(resolver, qPackId);
        return new ArrayList<>(Arrays.asList(arrayOfCardIds));
    }

    public static Long[] getQPackCardIds(ContentResolver resolver, Long qPackId) {
        Long [] result = null;

        Card.CardIdCursor c = new Card.CardIdCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_CARDS,
                        new String[] { CardsDbSchema.Cards.Cols.ID } , // projection
                        CardsDbSchema.Cards.Cols.QPACK_ID+" = ?",         // selection
                        new String[] { String.valueOf(qPackId) },              // selectionArgs
                        null                          // sortOrder
                ));

        return cardsIdArrFromCursor(c);
    }

    @NotNull
    private static Long[] cardsIdArrFromCursor(Card.CardIdCursor c) {
        Long[] result;
        try {
            result = new Long[c.getCount()];
            c.moveToFirst();
            for(int i=0; i<result.length; i++){
                result[i] = c.getId();
                c.moveToNext();
            }
        }finally {
            c.close();
        }

        return  result;
    }

    public static QPack getConcretePack(ContentResolver resolver, Long qPackId) {
        QPack.QPackCursor c = new QPack.QPackCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_QPACKS,
                        null , // projection
                        CardsDbSchema.Cards.Cols.ID+" = ?",         // selection
                        new String[] { String.valueOf(qPackId) },              // selectionArgs
                        null                          // sortOrder
        ));

        try {
            c.moveToFirst();
            if(!c.isAfterLast()){ return c.getQPack(); }
        }finally {
            c.close();
        }

        return  null;
    }

    /** TODO оптимизировать после перехода на Room (EXISTS, count()?) */
    public static boolean isAnyPackExists(ContentResolver resolver) {
        Cursor c = new QPack.QPackCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_QPACKS,
                        new String[]{CardsDbSchema.Cards.Cols.ID},
                        null,
                        null,
                        null
                ));

        try {
            return c.getCount() > 0;
        }finally {
            c.close();
        }
    }

    public static boolean isPackExists(ContentResolver resolver, Long qPackId) {
        Cursor c = new QPack.QPackCursor(
                resolver.query(
                        DbContentProvider.CONTENT_URI_QPACKS,
                        new String[]{CardsDbSchema.Cards.Cols.ID},
                        CardsDbSchema.Cards.Cols.ID+" = ?",
                        new String[] { String.valueOf(qPackId) },
                        null
                ));

        try {
            c.moveToFirst();
            if(!c.isAfterLast()){ return true; }
        }finally {
            c.close();
        }

        return false;
    }

    public static Long addNewTheme(ContentResolver resolver, Long parentThemeId, String title) {

        Theme newTheme = Theme.createNew(title, parentThemeId);
        Uri themeUri = resolver.insert(DbContentProvider.CONTENT_URI_THEMES, newTheme.getContentValues(false));
        Long resultId = Long.parseLong(themeUri.getLastPathSegment());
        return resultId;
    }

    public static void debugThemesInDb(ContentResolver resolver) {
        List<Theme> themes = ContentProviderHelper.getAllThemes(resolver);
        StringBuilder sb = new StringBuilder();
        for (Theme oneTheme:themes) {
            sb.append(oneTheme.toString()+"; ");
        }
        MyLog.add("tState: "+sb.toString());
    }

    // TODO TRANSACTION, or db restrictions
    //public static void deleteQPack(Context ctx, QPack qPack) {
    public static void deletePack(Context ctx, Long qPackId) {
        // курсы
        deleteCoursesByPackId(ctx, qPackId);

        // TODOS
        // CardTags
        Long[] cardIds = getQPackCardIds(ctx.getContentResolver(), qPackId);
        for(Long cardId:cardIds){
            deleteTodoByCardId(ctx, cardId);
            deleteCardTagsByCardId(ctx.getContentResolver(), cardId);
        }

        // карты
        int cnt;
        cnt = deleteCardsByQPackID(ctx, qPackId);

        // пак
        boolean result = deletePackOnly(ctx, qPackId);

        MyLog.add("cards deleted: "+cnt+", pack deleted?: "+result);
    }

    public static boolean deletePackOnly(Context ctx, Long qPackId) {
        ContentResolver resolver = ctx.getContentResolver();
        int cnt = resolver.delete(
                DbContentProvider.CONTENT_URI_QPACKS,
                CardsDbSchema.LearnCourse.Cols.ID+" = ?",
                new String[]{String.valueOf(qPackId)}
        );
        return cnt  > 0;
    }

    // TODO это временно тут
    public static LearnCourse getTempCourseFor(
            Context ctx, Long qPackId, List<Long> cardIds, boolean shuffleCards
    ){

        LearnCourse learnCourse = getTempCourse(ctx);
        Date date = DateUtils.getCurrentTimeDate();

        if(learnCourse == null){
            learnCourse = LearnCourse.createNewPreparing(
                    qPackId, "", LearnCourseMode.TEMPORARY, cardIds, Schedule.createEmpty(), date
            );
            addNewCourse(ctx, learnCourse);
        } else {
            learnCourse.change(qPackId, cardIds, date);
        }
        // TODO сомнительно, что этот тут должно быть
        learnCourse.prepareToCardsView(shuffleCards);
        saveCourse(ctx, learnCourse);
        return learnCourse;
    }


}
