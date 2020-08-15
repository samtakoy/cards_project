package ru.samtakoy.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import ru.samtakoy.core.database.CardsDbSchema;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.model.utils.MyStringUtils;
import ru.samtakoy.core.screens.log.MyLog;

public class LearnCourse {

    private static final Long TEMP_COURSE_ID = 0L;

    public static LearnCourse createNewPreparing(
            Long qPackId, String title,
            LearnCourseMode mode,
            @Nullable List<Long> cardIds,
            @Nullable Schedule restSchedule,
            @Nullable Date repeatDate
    ){
        return new LearnCourse().initNew(qPackId, title,
                mode,
                cardIds, restSchedule, repeatDate);
    }

    public static LearnCourse createNewAdditional(
            Long qPackId,
            String title,
            List<Long> cardIds,
            @NotNull Schedule restSchedule,
            int deltaMillis
            ){
        //int millisDelta = restSchedule.extractFirstItemInMillis();
        //Date repeatDate = DateUtils.dateFromDbSerialized (DateUtils.getDateAfterCurrentLong(millisDelta));
        return new LearnCourse().initNew(
                qPackId, title, LearnCourseMode.LEARN_WAITING,
                new ArrayList<>(cardIds), restSchedule,
                DateUtils.dateFromDbSerialized(DateUtils.getCurrentTimeLong() + deltaMillis)
        );
    }

    private Long mId;
    // пока привязываем к паку
    private Long mQPackId;
    private CourseType mCourseType;
    // исходный курс, который догоняем дополнительными карточками
    private Long mPrimaryCourseId;

    //
    private String mTitle;

    private LearnCourseMode mMode;
    private int mRepeatedCount;

    @NotNull
    private List<Long> mCardIds;
    @NotNull
    private Deque<Long> mTodoCardIds;
    // это для перепланирования, по окончании 1го цикла
    @NotNull
    private List<Long> mErrorCardIds;

    /** следующее расписание после выполения текущего запланируемого */
    @NotNull
    private Schedule mRestSchedule;
    /** то, что уже было запланировано */
    @NotNull
    private Schedule mRealizedSchedule;
    private Date mRepeatDate;

    /**
     * уже просмотренные в текущей сессии для
     * TODO перенести во ViewModel или нет, но тогда сохранять в DB */
    private Deque<Long> mViewedCardIds;


    public LearnCourse(){

    }

    private LearnCourse initNew(
            Long qPackId, String title, LearnCourseMode mode, @Nullable List<Long> cardIds, @Nullable Schedule restSchedule, @Nullable Date repeatDate
    ) {
        mId = TEMP_COURSE_ID;
        mQPackId = qPackId;
        // TODO пока заглушка
        mCourseType = CourseType.PRIMARY;
        // TODO пока заглушка
        mPrimaryCourseId = 0L;
        mTitle = title;
        mMode = mode;

        mRepeatedCount = 0;

        mCardIds = cardIds == null ? createNewEmptyCardIds() : new ArrayList<>(cardIds);
        mTodoCardIds = createNewEmptyTodoIds();
        mErrorCardIds = createNewEmptyErrorIds();

        mRestSchedule = restSchedule == null ? Schedule.createEmpty() : restSchedule;
        mRealizedSchedule = new Schedule();
        mRepeatDate = repeatDate == null ? DateUtils.getCurrentTimeDate() : repeatDate;


        mViewedCardIds = null;

        return this;
    }

    private static List<Long> createNewEmptyErrorIds() {
        return new ArrayList<Long>();
    }

    private static Deque<Long> createNewEmptyTodoIds() {
        return new ArrayDeque<Long>();
    }

    public void addCardsToCourse(List<Long> newCardsToAdd) {
        mCardIds.addAll(newCardsToAdd);
    }

    public float getProgress() {
        return 1 - mTodoCardIds.size() / (float)mCardIds.size();
    }

    public static class LearnPlanCursor extends CursorWrapper {
        public LearnPlanCursor(Cursor cursor){super(cursor);}
        public LearnCourse getLearnCourse(){

            LearnCourse result = new LearnCourse();
            result.mId = getLong(getColumnIndex(CardsDbSchema.LearnCourse.Cols.ID));
            result.mQPackId = getLong(getColumnIndex(CardsDbSchema.LearnCourse.Cols.QPACK_ID));
            result.mCourseType = CourseType.valueOfId(getInt(getColumnIndex(CardsDbSchema.LearnCourse.Cols.COURSE_TYPE)));
            result.mPrimaryCourseId = getLong(getColumnIndex(CardsDbSchema.LearnCourse.Cols.PRIMARY_COURSE_ID));

            result.mTitle = getString(getColumnIndex(CardsDbSchema.LearnCourse.Cols.TITLE));
            result.mMode = LearnCourseMode.valueOfId(getInt(getColumnIndex(CardsDbSchema.LearnCourse.Cols.MODE)));
            result.mRepeatedCount = getInt(getColumnIndex(CardsDbSchema.LearnCourse.Cols.REPEATED_COUNT));

            result.mCardIds = (List<Long>)cardIdsFromString(getString(getColumnIndex(CardsDbSchema.LearnCourse.Cols.CARD_IDS)), createNewEmptyCardIds());
            result.mTodoCardIds = (Deque<Long>)cardIdsFromString(getString(getColumnIndex(CardsDbSchema.LearnCourse.Cols.TODO_CARD_IDS)), createNewEmptyTodoIds());
            result.mErrorCardIds = (List<Long>)cardIdsFromString(getString(getColumnIndex(CardsDbSchema.LearnCourse.Cols.ERROR_CARD_IDS)), createNewEmptyErrorIds());

            result.mRestSchedule = Schedule.fromString(getString(getColumnIndex(CardsDbSchema.LearnCourse.Cols.REST_SCHEDULE)));
            result.mRealizedSchedule = Schedule.fromString(getString(getColumnIndex(CardsDbSchema.LearnCourse.Cols.REALIZED_SCHEDULE)));
            result.mRepeatDate = new Date(getLong(getColumnIndex(CardsDbSchema.LearnCourse.Cols.REPEAT_DATE)));

            // просмотренные в текущей сессии, должны быть проинициализированы
            result.mViewedCardIds = createNewEmptyViewedCardIds();

            return result;
        }
    }

    @NotNull
    private static ArrayList<Long> createNewEmptyCardIds() {
        return new ArrayList<Long>();
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues values = new ContentValues();
        if(withId){
            values.put(CardsDbSchema.LearnCourse.Cols.ID, mId);
            MyLog.add("__id:"+mId);
        } else {
            MyLog.add("__NO_ID__");
        }
        values.put(CardsDbSchema.LearnCourse.Cols.QPACK_ID, mQPackId);
        values.put(CardsDbSchema.LearnCourse.Cols.COURSE_TYPE, mCourseType.getId());
        values.put(CardsDbSchema.LearnCourse.Cols.PRIMARY_COURSE_ID, mPrimaryCourseId);
        values.put(CardsDbSchema.LearnCourse.Cols.TITLE, mTitle);
        values.put(CardsDbSchema.LearnCourse.Cols.MODE, mMode.getId());
        values.put(CardsDbSchema.LearnCourse.Cols.REPEATED_COUNT, mRepeatedCount);

        values.put(CardsDbSchema.LearnCourse.Cols.CARD_IDS, cardIdsToString(mCardIds));
        values.put(CardsDbSchema.LearnCourse.Cols.TODO_CARD_IDS, cardIdsToString(mTodoCardIds));
        values.put(CardsDbSchema.LearnCourse.Cols.ERROR_CARD_IDS, cardIdsToString(mErrorCardIds));

        values.put(CardsDbSchema.LearnCourse.Cols.REST_SCHEDULE, mRestSchedule.toString());
        values.put(CardsDbSchema.LearnCourse.Cols.REALIZED_SCHEDULE, mRealizedSchedule.toString());
        values.put(CardsDbSchema.LearnCourse.Cols.REPEAT_DATE, DateUtils.dateToDbSerialized(mRepeatDate));

        return values;
    }

    public Long getId() {
        return mId;
    }
    public void setId(Long id) {
        mId = id;
    }
    public Long getQPackId() {
        return mQPackId;
    }
    public void change(Long qPackId, List<Long> cardIds, Date repeatDate) {
        mQPackId = qPackId;
        mCardIds = cardIds == null ? createNewEmptyCardIds() : new ArrayList<>(cardIds);
        mRepeatDate = repeatDate == null ? DateUtils.getCurrentTimeDate() : repeatDate;
    }
    public boolean hasQPackId(){
        return mQPackId > 0;
    }
    public String getTitle() { return mTitle; }
    public LearnCourseMode getMode() { return mMode; }
    public int getRepeatedCount() { return mRepeatedCount; }
    public List<Long> getCardIds() { return mCardIds; }
    @Nullable
    public List<Long> getErrorCardIds() { return mErrorCardIds; }
    //@Nullable
    //public List<Long> getTodoCardIds() {return mTodoCardIds;}
    public Schedule getRestSchedule() { return mRestSchedule; }
    public Schedule getRealizedSchedule() { return mRealizedSchedule; }
    public void setRestSchedule(Schedule sh) { mRestSchedule = sh; }
    public Date getRepeatDate() {
        return mRepeatDate;
    }
    public void setRepeatDate(Date value) {
        mRepeatDate = value;
    }
    public long getRepeatDateUTCMillis() {
        return DateUtils.dateToDbSerialized(mRepeatDate);
    }

    public long getRepeatDateDebug() {
        return LearnCourse.getRepeatDateDebug(this);
    }
    public static long getRepeatDateDebug(LearnCourse learnCourse) {
        return DateUtils.dateToDbSerialized(learnCourse.getRepeatDate())/1000;
    }

    public void toLearnMode(){
        mMode = LearnCourseMode.LEARNING;
        makeInitialTodos(false);
        mErrorCardIds = createNewEmptyErrorIds();
        mViewedCardIds = createNewEmptyViewedCardIds();
    }

    @NotNull
    private static ArrayDeque<Long> createNewEmptyViewedCardIds() {
        return new ArrayDeque<>();
    }

    public void toRepeatMode(){
        mMode = LearnCourseMode.REPEATING;
        makeInitialTodos(true);
        mErrorCardIds = createNewEmptyErrorIds();
        mViewedCardIds = createNewEmptyViewedCardIds();
    }

    public void finishLearnOrRepeat(long currentSystemTimeMillis){

        if(mRestSchedule.isEmpty()){
            mMode = LearnCourseMode.COMPLETED;
        } else {
            if(mMode != LearnCourseMode.LEARNING){
                mRepeatedCount++;
            }
            mMode = LearnCourseMode.REPEAT_WAITING;
            setNextRepeatDateFrom(currentSystemTimeMillis);
        }
    }

    private void setNextRepeatDateFrom(long currentSystemTimeMillis) {
        if(!mRestSchedule.isEmpty()) {
            mRealizedSchedule.addItem(mRestSchedule.getFirstItem());
            long nextDateLong = mRestSchedule.extractFirstItemInMillis() + currentSystemTimeMillis;
            mRepeatDate = DateUtils.dateFromDbSerialized(nextDateLong);

            MyLog.add("setNextRepeatDateFrom, cur:"+(currentSystemTimeMillis/1000)+", newD:"+getRepeatDateDebug());
        }
    }

    private static String cardIdsToString(Collection<Long> cardIds){
        return MyStringUtils.join(cardIds.iterator(), ",");
    }

    private static Collection<Long> cardIdsFromString(String srcString, Collection<Long> consumer){
        if(srcString == null){
            return consumer;
        }
        String[] separated = StringUtils.split(srcString, ",");
        for (String strNumber : separated) {
            consumer.add(Long.valueOf(strNumber));
        }
        return consumer;
    }

    public boolean isAlreadyInErrors(Long cardId){
        return mErrorCardIds.contains(cardId);
    }

    public void makeViewedWithError() throws Exception {

        if(mTodoCardIds.isEmpty()){
            throw new Exception("todo cards is empty");
        }
        Long cardId = mTodoCardIds.peekFirst();
        // ошибочную в конец - пока просто так
        if(!isAlreadyInErrors(cardId)){
            mErrorCardIds.add(cardId);
        }
        makeViewedCurCard();
    }

    public Long peekCurCardToView(){
        if(mTodoCardIds.isEmpty()){
            // TODO exception
            return -1L;
        }
        return mTodoCardIds.peekFirst();
    }

    public void makeViewedCurCard() throws Exception {
        if(mTodoCardIds.isEmpty()){
            throw new Exception("todo cards is empty");
        }
        mViewedCardIds.addLast(mTodoCardIds.removeFirst());
    }



    public boolean rollback(){
        if(!canRollback()){
            return false;
        }
        mTodoCardIds.addFirst(mViewedCardIds.removeLast());
        return true;
    }

    public boolean canRollback(){
        return mViewedCardIds.size() > 0;
    }

    public boolean hasTodoCards(){
        return mTodoCardIds.size() > 0;
    }

    public boolean isLastCard(){
        return mTodoCardIds.size() == 1;
    }

    public boolean hasRealId() {
        return mId > 0;
    }

    /**
     * TODO данный метод только для фейкового просмотра?
     * */
    public void prepareToCardsView(boolean shuffleCards){
        makeInitialTodos(shuffleCards);
        mErrorCardIds = createNewEmptyErrorIds();
        mViewedCardIds = createNewEmptyViewedCardIds();
    }

    private void makeInitialTodos(boolean shuffleCards) {
        List<Long> shuffledIds;

        if(shuffleCards){
            shuffledIds = createShuffledIds();
        } else{
            shuffledIds = mCardIds;
        }
        mTodoCardIds = createNewEmptyTodoIds();
        for(Long id:shuffledIds){
            mTodoCardIds.addLast(id);
        }
    }

    private List<Long> createShuffledIds(){
        List<Long> shuffledIds = new ArrayList<>(mCardIds);
        Collections.shuffle(shuffledIds);
        return shuffledIds;
    }

    public int getCardsCount() {
        return mCardIds.size();
    }

    public int getViewedCardsCount() {
        /* ?
        if(mTodoCardIds == null){
            return 0;
        }/**/

        return mCardIds.size() - mTodoCardIds.size();
    }

    public int getErrorCardsCount() {
        if(mErrorCardIds == null){
            return 0;
        }
        return mErrorCardIds.size();
    }

    public boolean hasRestSchedule() {
        return mRestSchedule != null && !mRestSchedule.isEmpty();
    }

    public long getMillisToStart() {
        return DateUtils.getMillisTo(mRepeatDate);
    }

    public boolean isAllCardsIn(List<Long> cardIds) {
        return mCardIds.containsAll(cardIds);
    }

    public boolean hasNotInCards(List<Long> cardIds) {
        for(Long cardId:cardIds){
            if(!mCardIds.contains(cardId)){
                return true;
            }
        }
        return false;
    }

    public List<Long> getNotInCards(List<Long> cardIds) {
        List<Long> result = new ArrayList<>();
        for(Long cardId:cardIds){
            if(!mCardIds.contains(cardId)){
                result.add(cardId);
            }
        }
        return result;
    }

    public boolean hasRealizedSchedule(){
        return mRealizedSchedule != null && !mRealizedSchedule.isEmpty();
    }


    // TODO временно
    public String getDynamicTitle(){
        // активные
        if(mMode == LearnCourseMode.LEARNING || mMode == LearnCourseMode.REPEATING){
            return "(a)"+getTitle();
        }else
        if(mMode == LearnCourseMode.LEARN_WAITING || mMode == LearnCourseMode.REPEAT_WAITING){
            if(getMillisToStart() <= 0){
                return "(!)"+getTitle();
            }
        }
        return getTitle();
    }

}
