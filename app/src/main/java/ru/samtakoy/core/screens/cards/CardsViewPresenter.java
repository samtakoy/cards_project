package ru.samtakoy.core.screens.cards;

import java.util.HashMap;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.di.components.AppComponent;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.screens.cards.types.CardViewMode;
import ru.samtakoy.core.screens.log.MyLog;

@InjectViewState
public class CardsViewPresenter extends MvpPresenter<CardsViewView> {


    private LearnCourse mCourse;
    private CardViewMode mViewMode;
    private boolean mOnAnswer;

    private HashMap<Long, BackupInfo> mBackups;

    @Inject
    CardsInteractor mCardsInteractor;
    @Inject
    NCoursesInteractor mCoursesInteractor;
    @Inject
    CoursesPlanner mCoursesPlanner;



    public CardsViewPresenter(

            AppComponent appComponent,
            Long learnCourseId,
            CardViewMode viewMode
    ){
        //mCourse = learnCourse;
        mViewMode = viewMode;
        mOnAnswer = false;

        mBackups = new HashMap<>();

        appComponent.inject(this);

        mCourse = mCoursesInteractor.getCourse(learnCourseId);


        switchScreenToCurCard(false);

    }

    private void saveResult() {
        //if(mCourse.hasRealId()) {
            mCoursesInteractor.saveCourse(mCourse);
        //}
    }

    private void onAfterCardComplete() {
        if(!mCourse.hasTodoCards()){
            finishCards();
        } else {
            mOnAnswer = false;
            saveResult();
            switchScreenToCurCard(false);
        }
    }

    private void finishCards() {

        MyLog.add("finishCards");

        long currentTimeLong = DateUtils.getCurrentTimeLong();

        mCourse.finishLearnOrRepeat(currentTimeLong);
        saveResult();

        mCardsInteractor.saveQPackLastViewDate(mCourse.getQPackId(), currentTimeLong, true);

        // перепланировать следующий курс
        if(mCourse.hasRealId()) {
            mCoursesPlanner.reScheduleLearnCourses();
        }

        getViewState().switchScreenToResults(mCourse.getId(), mViewMode);
    }

    private void switchScreenToCurCard(boolean backAnimation){

        getViewState().switchScreenToCard(
                mCourse.getQPackId(), mCourse.peekCurCardToView(),
                mViewMode, mOnAnswer, backAnimation,
                mCourse.isLastCard()
        );

        getViewState().showProgress(mCourse.getViewedCardsCount(), mCourse.getCardsCount(), mOnAnswer);

        updateRevertButton();
    }

    private void updateRevertButton() {

        Long curCardId = mCourse.peekCurCardToView();
        BackupInfo backupInfo = mBackups.get(curCardId);
        getViewState().showRevertButton(
                backupInfo != null && backupInfo.hasBackup(mOnAnswer)
        );
    }

    // ----------------------------------------------------------------------------------------

    void onUiPrevCard(){
        if(mCourse.rollback()){
            mOnAnswer = false;
            saveResult();
            switchScreenToCurCard(true);
        }
    }

    void onUiViewAnswer() {
        mOnAnswer = true;
        switchScreenToCurCard(false);
    }

    void onUiNextCard(){
        try{
            mCourse.makeViewedCurCard();
        } catch (Exception e){
            getViewState().showError(R.string.cards_view_some_err_msg);
        }
        onAfterCardComplete();
    }

    void onUiWrongAnswer(){
        try {
            mCourse.makeViewedWithError();
        } catch(Exception e){
            getViewState().showError(R.string.cards_view_some_err_msg);
        }
        saveResult();
        onAfterCardComplete();
    }

    void onUiBackToQuestion(){
        mOnAnswer = false;
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
        Card card = mCardsInteractor.getCard(mCourse.peekCurCardToView());
        return card.getQuestion();
    }

    private String getCurCardAnswerText(){
        Card card = mCardsInteractor.getCard(mCourse.peekCurCardToView());
        return card.getAnswer();
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
        Card card = mCardsInteractor.getCard(mCourse.peekCurCardToView());
        backupInfo.setQuestionIfEmpty(card.getQuestion());

        mCardsInteractor.setCardNewQuestionText(mCourse.peekCurCardToView(), text);
        updateRevertButton();
    }

    public void onNewAnswerText(String text) {

        BackupInfo backupInfo = getOrCreateBackupInfo();
        Card card = mCardsInteractor.getCard(mCourse.peekCurCardToView());
        backupInfo.setAnswerIfEmpty(card.getAnswer());

        mCardsInteractor.setCardNewAnswerText(mCourse.peekCurCardToView(), text);
        updateRevertButton();
    }

    public void onUiRevertClick() {

        BackupInfo backupInfo = getOrCreateBackupInfo();

        if(backupInfo.hasBackup(mOnAnswer)) {

            if (mOnAnswer) {
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
