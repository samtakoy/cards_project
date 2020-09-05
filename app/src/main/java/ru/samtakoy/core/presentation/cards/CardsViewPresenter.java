package ru.samtakoy.core.presentation.cards;

import android.os.Parcelable;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.database.room.entities.CardEntity;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.elements.Schedule;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.core.utils.DateUtils;

import static ru.samtakoy.core.business.utils.TransformersKt.c_io_mainThread;
import static ru.samtakoy.core.business.utils.TransformersKt.f_io_mainThread;
import static ru.samtakoy.core.business.utils.TransformersKt.s_io_mainThread;

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

    private Long mCourseId;
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

        mCourseId = learnCourseId;
        mViewMode = viewMode;

        mBackups = new HashMap<>();


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

        blockUiAndRunOpt(
                mCoursesInteractor.getCourse(mCourseId)
                        .compose(s_io_mainThread())
                        .subscribe(
                                learnCourse -> {
                                    unblockUi();
                                    mCourse = learnCourse;
                                    onStateRestored();
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onNoRestoreState() {
        mState = new CardsViewState();

        blockUiAndRunOpt(
                mCoursesInteractor.getCourse(mCourseId)
                        .compose(s_io_mainThread())
                        .subscribe(
                                learnCourse -> {
                                    unblockUi();
                                    mCourse = learnCourse;
                                    onEmptyStateRestored();
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onStateRestored() {
        if (mCourse.hasTodoCards()) {
            switchScreenToCurCard(AnimationType.OFF, mState.isOnAnswer());
        } else {
            getViewState().switchScreenToResults(mCourse.getId(), mViewMode, AnimationType.OFF);
        }
    }

    private void onEmptyStateRestored() {
        switchScreenToCurCard(AnimationType.FORWARD, mState.isOnAnswer());
    }

    private void onAfterCardComplete() {
        if (!mCourse.hasTodoCards()) {
            finishCards();
        } else {
            blockUiAndRunOpt(
                    mCoursesInteractor.saveCourse(mCourse)
                            .compose(c_io_mainThread())
                            .subscribe(
                                    () -> {
                                        unblockUi();
                                        switchScreenToCurCard(AnimationType.FORWARD, false);
                                    },
                                    throwable -> onGetError(throwable)
                            )
            );
        }
    }

    private void finishCards() {

        if (isOperationInProgress()) {
            return;
        }

        MyLog.add("finishCards");
        Date currentTime = DateUtils.getCurrentTimeDate();

        blockUiAndRunOpt(
                mCoursesInteractor.finishCourseCardsViewing(mCourse, currentTime)
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> {
                                    unblockUi();
                                    getViewState().switchScreenToResults(mCourse.getId(), mViewMode, AnimationType.FORWARD);
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void switchScreenToCurCard(AnimationType aType, boolean onAnswer) {


        mGetCardSubscription.clear();
        mGetCardSubscription.add(
                mCardsInteractor.getCardRx(mCourse.peekCurCardToView())
                        .onBackpressureLatest()
                        .compose(f_io_mainThread())
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
            blockUiAndRunOpt(
                    mCoursesInteractor.saveCourse(mCourse)
                            .compose(c_io_mainThread())
                            .subscribe(
                                    () -> {
                                        unblockUi();
                                        switchScreenToCurCard(AnimationType.BACK, false);
                                    },
                                    throwable -> onGetError(throwable)
                            )
            );
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
        onAfterCardComplete();
    }

    void onUiBackToQuestion() {
        switchScreenToCurCard(AnimationType.BACK, false);
    }

    void onUiResultOk(Schedule newErrorCardsSchedule) {

        if (mViewMode != CardViewMode.LEARNING && mCourse.getErrorCardsCount() > 0 && !newErrorCardsSchedule.isEmpty()) {
            /*
            // запланировать добавочные показы ошибочных карт
            blockUiAndRunOpt(
                    mCoursesPlanner.planAdditionalCards( mCourse.getQPackId(), mCourse.getErrorCardIds(), newErrorCardsSchedule )
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> {
                                    unblockUi();
                                    getViewState().closeScreen();
                                },
                                throwable -> onGetError(throwable)
                        )
            );*/
            mCoursesPlanner.planAdditionalCards(mCourse.getQPackId(), mCourse.getErrorCardIds(), newErrorCardsSchedule);
            getViewState().closeScreen();
        } else {
            getViewState().closeScreen();
        }
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

        blockUiAndRunOpt(
                mCardsInteractor
                        .setCardNewQuestionTextRx(mCourse.peekCurCardToView(), newText)
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> {
                                    unblockUi();
                                    getOrCreateBackupInfo().setQuestionIfEmpty(prevText);
                                    updateRevertButton();
                                },
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onNewAnswerText(String newText) {


        String prevText = mCardOnScreen.getAnswer();

        blockUiAndRunOpt(
                mCardsInteractor
                        .setCardNewAnswerTextRx(mCourse.peekCurCardToView(), newText)
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> {
                                    unblockUi();
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

                blockUiAndRunOpt(
                        mCardsInteractor.setCardNewAnswerTextRx(mCourse.peekCurCardToView(), backupInfo.getAnswer())
                                .compose(c_io_mainThread())
                                .subscribe(
                                        () -> {
                                            unblockUi();
                                            backupInfo.resetAnswer();
                                            updateRevertButton();
                                        },
                                        throwable -> onGetError(throwable)
                                )
                );

            } else {

                blockUiAndRunOpt(
                        mCardsInteractor.setCardNewQuestionTextRx(mCourse.peekCurCardToView(), backupInfo.getQuestion())
                                .compose(c_io_mainThread())
                                .subscribe(
                                        () -> {
                                            unblockUi();
                                            backupInfo.resetQuestion();
                                            updateRevertButton();
                                        },
                                        throwable -> onGetError(throwable)
                                )
                );
            }
        }

    }


    private void onGetError(Throwable t) {
        unblockUi();
        MyLog.add(ExceptionUtils.getMessage(t), t);
        getViewState().showError(R.string.db_request_err_message);
    }

    private boolean isOperationInProgress() {
        return mOperationDisposable.size() > 0;
    }

    private void blockUi() {
        getViewState().blockScreenOnOperation();
    }

    private void unblockUi() {
        getViewState().unblockScreenOnOperation();
        mOperationDisposable.clear();
    }

    private void blockUiAndRunOpt(@NonNull Disposable disposable) {

        if (isOperationInProgress()) {
            MyLog.add("CardsViewPresenter: wrong ui logic");
            disposable.dispose();
            return;
        }

        blockUi();
        mOperationDisposable.clear();
        mOperationDisposable.add(disposable);
    }

}
