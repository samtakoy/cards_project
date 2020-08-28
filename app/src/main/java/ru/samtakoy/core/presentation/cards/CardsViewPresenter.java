package ru.samtakoy.core.presentation.cards;

import android.os.Parcelable;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.database.room.entities.CardEntity;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.elements.Schedule;
import ru.samtakoy.core.database.room.entities.types.CourseType;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.core.utils.DateUtils;

@InjectViewState
public class CardsViewPresenter extends MvpPresenter<CardsViewView> {

    CardsInteractor mCardsInteractor;
    NCoursesInteractor mCoursesInteractor;
    CoursesPlanner mCoursesPlanner;

    private LearnCourseEntity mCourse;
    private CardViewMode mViewMode;

    public static class Factory {

        @Inject
        CardsInteractor mCardsInteractor;
        @Inject
        NCoursesInteractor mCoursesInteractor;
        @Inject
        CoursesPlanner mCoursesPlanner;

        @Inject
        public Factory() {
        }

        public CardsViewPresenter create(
                Long learnCourseId,
                CardViewMode viewMode
        ) {
            return new CardsViewPresenter(
                    mCardsInteractor, mCoursesInteractor, mCoursesPlanner, learnCourseId, viewMode
            );
        }
    }

    private HashMap<Long, BackupInfo> mBackups;

    private CardsViewState mState;
    private CardEntity mCurCard;
    private CompositeDisposable mRequestCurCardDisposable = new CompositeDisposable();

    public CardsViewPresenter(

            CardsInteractor cardsInteractor,
            NCoursesInteractor coursesInteractor,
            CoursesPlanner coursesPlanner,

            Long learnCourseId,
            CardViewMode viewMode
    ) {

        mCardsInteractor = cardsInteractor;
        mCoursesInteractor = coursesInteractor;
        mCoursesPlanner = coursesPlanner;

        mViewMode = viewMode;

        mBackups = new HashMap<>();

        mCourse = mCoursesInteractor.getCourse(learnCourseId);
    }


    @Override
    public void onDestroy() {

        mRequestCurCardDisposable.dispose();
        super.onDestroy();
    }

    public Parcelable getStateToSave() {
        return mState;
    }

    public void onRestoreState(Parcelable state) {
        mState = (CardsViewState) state;
        switchScreenToCurCard(false);
    }

    public void onNoRestoreState() {
        mState = new CardsViewState();
        switchScreenToCurCard(false);
    }

    private void saveResult() {
        if (mCoursesInteractor.saveCourse(mCourse)) {
            MyLog.add("Course " + mCourse.getId() + " saved");
        } else {
            MyLog.add("Course " + mCourse.getId() + " save error!");
        }
    }

    private void onAfterCardComplete() {
        if (!mCourse.hasTodoCards()) {
            finishCards();
        } else {
            mState.setOnAnswer(false);
            saveResult();
            switchScreenToCurCard(false);
        }
    }

    private void finishCards() {

        MyLog.add("finishCards");

        Date currentTime = DateUtils.getCurrentTimeDate();

        mCourse.finishLearnOrRepeat(currentTime);
        saveResult();

        mCardsInteractor.saveQPackLastViewDate(mCourse.getQPackId(), currentTime, true);

        // перепланировать следующий курс
        //if (mCourse.hasRealId()) {
        if (mCourse.getCourseType() != CourseType.TEMPORARY) {
            mCoursesPlanner.reScheduleLearnCourses();
        }

        getViewState().switchScreenToResults(mCourse.getId(), mViewMode);
    }

    private void switchScreenToCurCard(boolean backAnimation) {

        mRequestCurCardDisposable.clear();
        mRequestCurCardDisposable.add(
                mCardsInteractor.getCardRx(mCourse.peekCurCardToView())
                        .onBackpressureLatest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                cardEntity -> {
                                    mCurCard = cardEntity;
                                    switchScreenToCurCardNow(backAnimation);
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onGetError(Throwable t) {
        MyLog.add(ExceptionUtils.getMessage(t));
        getViewState().showError(R.string.db_request_err_message);
    }

    private void switchScreenToCurCardNow(boolean backAnimation) {

        getViewState().switchScreenToCard(
                mCourse.getQPackId(), mCourse.peekCurCardToView(),
                mViewMode, mState.isOnAnswer(), backAnimation,
                mCourse.isLastCard()
        );

        getViewState().showProgress(mCourse.getViewedCardsCount(), mCourse.getCardsCount(), mState.isOnAnswer());

        updateRevertButton();
    }

    private void updateRevertButton() {

        Long curCardId = mCourse.peekCurCardToView();
        BackupInfo backupInfo = mBackups.get(curCardId);
        getViewState().showRevertButton(
                backupInfo != null && backupInfo.hasBackup(mState.isOnAnswer())
        );
    }

    // ----------------------------------------------------------------------------------------

    void onUiPrevCard() {
        if (mCourse.rollback(mState.getViewedCardIds())) {
            mState.setOnAnswer(false);
            saveResult();
            switchScreenToCurCard(true);
        }
    }

    void onUiViewAnswer() {
        mState.setOnAnswer(true);
        switchScreenToCurCard(false);
    }

    void onUiNextCard(){
        try{
            mCourse.makeViewedCurCard(mState.getViewedCardIds());
        } catch (Exception e){
            MyLog.add("CardsViewPresenter::onUiNextCard", e);
            getViewState().showError(R.string.cards_view_some_err_msg);
        }
        onAfterCardComplete();
    }

    void onUiWrongAnswer(){
        try {
            mCourse.makeViewedWithError(mState.getViewedCardIds());
        } catch(Exception e){
            MyLog.add("CardsViewPresenter::onUiWrongAnswer", e);
            getViewState().showError(R.string.cards_view_some_err_msg);
        }
        saveResult();
        onAfterCardComplete();
    }

    void onUiBackToQuestion(){
        mState.setOnAnswer(false);
        switchScreenToCurCard(true);
    }

    void onUiResultOk(Schedule newSchedule) {
        if(mViewMode != CardViewMode.LEARNING && mCourse.getErrorCardsCount()>0 && !newSchedule.isEmpty()){
            // запланировать добавочные показы ошибочных карт
            mCoursesPlanner.planAdditionalCards(
                    mCourse.getQPackId(),
                    mCourse.getErrorCardIds(),
                    newSchedule
            );
        }
        getViewState().closeScreen();
    }

    private String getCurCardQuestionText(){
        return mCurCard.getQuestion();
    }

    private String getCurCardAnswerText(){
        return mCurCard.getAnswer();
    }

    public void onUiQuestionEditTextClick() {
        getViewState().showEditTextDialog( getCurCardQuestionText(), true );
    }

    public void onUiAnswerEditTextClick() {
        getViewState().showEditTextDialog( getCurCardAnswerText(), false );
    }

    private BackupInfo getOrCreateBackupInfo() {
        Long cardId = mCourse.peekCurCardToView();
        BackupInfo backupInfo = mBackups.get(cardId);
        if(backupInfo == null){
            backupInfo = new BackupInfo();
            mBackups.put(cardId, backupInfo);
        }
        return backupInfo;
    }

    public void onNewQuestionText(String text) {

        BackupInfo backupInfo = getOrCreateBackupInfo();
        backupInfo.setQuestionIfEmpty(mCurCard.getQuestion());

        mCardsInteractor.setCardNewQuestionText(mCourse.peekCurCardToView(), text);
        updateRevertButton();
    }

    public void onNewAnswerText(String text) {

        BackupInfo backupInfo = getOrCreateBackupInfo();
        backupInfo.setAnswerIfEmpty(mCurCard.getAnswer());

        mCardsInteractor.setCardNewAnswerText(mCourse.peekCurCardToView(), text);
        updateRevertButton();
    }

    public void onUiRevertClick() {

        BackupInfo backupInfo = getOrCreateBackupInfo();

        if (backupInfo.hasBackup(mState.isOnAnswer())) {

            if (mState.isOnAnswer()) {
                mCardsInteractor.setCardNewAnswerText(mCourse.peekCurCardToView(), backupInfo.getAnswer());
                backupInfo.resetAnswer();
            } else {
                mCardsInteractor.setCardNewQuestionText(mCourse.peekCurCardToView(), backupInfo.getQuestion());
                backupInfo.resetQuestion();
            }
        }
        updateRevertButton();
    }
}
