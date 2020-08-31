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

    public enum AnimationType {
        FORWARD,
        BACK,
        OFF
    }

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
    private CardEntity mCardOnScreen;
    private CompositeDisposable mGetCardSubscription = new CompositeDisposable();
    private CompositeDisposable mOperationDisposable = new CompositeDisposable();

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

        mGetCardSubscription.dispose();
        mOperationDisposable.dispose();
        super.onDestroy();
    }

    public Parcelable getStateToSave() {
        return mState;
    }

    public void onRestoreState(Parcelable state) {
        mState = (CardsViewState) state;

        if (mCourse.hasTodoCards()) {
            switchScreenToCurCard(AnimationType.OFF, mState.isOnAnswer());
        } else {
            getViewState().switchScreenToResults(mCourse.getId(), mViewMode, AnimationType.OFF);
        }

    }
    public void onNoRestoreState() {
        mState = new CardsViewState();
        switchScreenToCurCard(AnimationType.FORWARD, mState.isOnAnswer());
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
            saveResult();
            switchScreenToCurCard(AnimationType.FORWARD, false);
        }
    }

    private void finishCards() {

        MyLog.add("finishCards");

        Date currentTime = DateUtils.getCurrentTimeDate();

        // TODO - эта логика должна уехать в interactor
        mCourse.finishLearnOrRepeat(currentTime);
        saveResult();

        mCardsInteractor.saveQPackLastViewDate(mCourse.getQPackId(), currentTime, true);

        // перепланировать следующий курс
        //if (mCourse.hasRealId()) {
        if (mCourse.getCourseType() != CourseType.TEMPORARY) {
            mCoursesPlanner.reScheduleLearnCourses();
        }

        getViewState().switchScreenToResults(mCourse.getId(), mViewMode, AnimationType.FORWARD);
    }

    private void switchScreenToCurCard(AnimationType aType, boolean onAnswer) {


        mGetCardSubscription.clear();
        mGetCardSubscription.add(
                mCardsInteractor.getCardRx(mCourse.peekCurCardToView())
                        .onBackpressureLatest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                cardEntity -> onCardUpdated(aType, onAnswer, cardEntity),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onCardUpdated(AnimationType aType, boolean onAnswer, CardEntity cardEntity) {
        boolean isNewState =
                mCardOnScreen == null
                        || (mCardOnScreen.getId() != cardEntity.getId())
                        || (onAnswer != mState.isOnAnswer());

        mCardOnScreen = cardEntity;
        mState.setOnAnswer(onAnswer);

        if (!isNewState) {
            aType = AnimationType.OFF;
        }

        switchScreenToCurCardNow(aType);
    }

    private void onGetError(Throwable t) {
        MyLog.add(ExceptionUtils.getMessage(t), t);
        getViewState().showError(R.string.db_request_err_message);
    }

    private void switchScreenToCurCardNow(AnimationType aType) {

        getViewState().switchScreenToCard(
                mCourse.getQPackId(), mCourse.peekCurCardToView(),
                mViewMode, mState.isOnAnswer(), aType,
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
            saveResult();
            switchScreenToCurCard(AnimationType.BACK, false);
        }
    }

    void onUiViewAnswer() {
        switchScreenToCurCard(AnimationType.FORWARD, true);
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
        switchScreenToCurCard(AnimationType.BACK, false);
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
        return mCardOnScreen.getQuestion();
    }

    private String getCurCardAnswerText(){
        return mCardOnScreen.getAnswer();
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

    public void onNewQuestionText(String newText) {

        String prevText = mCardOnScreen.getQuestion();

        mOperationDisposable.clear();
        mOperationDisposable.add(
                mCardsInteractor
                        .setCardNewQuestionText(mCourse.peekCurCardToView(), newText)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    getOrCreateBackupInfo().setQuestionIfEmpty(prevText);
                                    updateRevertButton();
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onNewAnswerText(String newText) {

        String prevText = mCardOnScreen.getAnswer();

        mOperationDisposable.clear();
        mOperationDisposable.add(
                mCardsInteractor
                        .setCardNewAnswerText(mCourse.peekCurCardToView(), newText)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    getOrCreateBackupInfo().setAnswerIfEmpty(prevText);
                                    updateRevertButton();
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onUiRevertClick() {


        BackupInfo backupInfo = getOrCreateBackupInfo();

        if (backupInfo.hasBackup(mState.isOnAnswer())) {

            if (mState.isOnAnswer()) {

                mOperationDisposable.clear();
                mOperationDisposable.add(
                        mCardsInteractor.setCardNewAnswerText(mCourse.peekCurCardToView(), backupInfo.getAnswer())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            backupInfo.resetAnswer();
                                            updateRevertButton();
                                        },
                                        throwable -> onGetError(throwable)
                                )
                );

            } else {

                mOperationDisposable.clear();
                mOperationDisposable.add(
                        mCardsInteractor.setCardNewQuestionText(mCourse.peekCurCardToView(), backupInfo.getQuestion())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            backupInfo.resetQuestion();
                                            updateRevertButton();
                                        },
                                        throwable -> onGetError(throwable)
                                )
                );
            }
        }

    }
}
