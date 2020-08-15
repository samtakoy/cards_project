package ru.samtakoy.core.screens.qpack;


import java.util.List;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.di.components.AppComponent;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.QPack;

@InjectViewState
public class QPackInfoPresenter extends MvpPresenter<QPackInfoView> {

    private QPack mQPack;

    @Inject
    public CardsInteractor mCardsInteractor;
    @Inject
    public NCoursesInteractor mCoursesInteractor;

    //public QPackInfoPresenter(CurrentCourseHolder currentCourseHolder, CardsInteractor cardsInteractor, NCoursesInteractor coursesInteractor){
    public QPackInfoPresenter(AppComponent appComponent, Long qPackId){


        appComponent.inject(this);

        appComponent.inject(this);

        setup(qPackId);
    }

    private QPackInfoPresenter setup(Long qPackId){
        mQPack = mCardsInteractor.getQPack(qPackId);

        getViewState().initView(mQPack.getTitle(), String.valueOf(mCardsInteractor.getQPackCardCount(qPackId)));
        return this;
    }

    private boolean hasPackCards(){
        return mCardsInteractor.hasPackCards(mQPack.getId());
    }


    // ++++++++++++++++++++++++++++++++++++++++++++++++++
    // from UI
    // ++++++++++++++++++++++++++++++++++++++++++++++++++

    public void onDeletePack(){

        mCardsInteractor.deleteQPack(mQPack.getId());
        getViewState().closeScreen();
    }

    public void onNewCourseCommit(String courseTitle) {
        Long courseId = mCoursesInteractor.addCourseForQPack(courseTitle, mQPack.getId());
        getViewState().showCourseScreen(courseId);
    }

    public void onShowPackCourses(){
        getViewState().showCourses(mQPack);
    }

    public void onAddToNewCourse() {
        if(!hasPackCards()){
            getViewState().showMessage(R.string.msg_there_is_no_cards_in_pack);
            return;
        }
        getViewState().requestNewCourseCreation(mQPack.getTitle());
    }

    public void onAddToExistsCourse() {
        if(!hasPackCards()){
            getViewState().showMessage(R.string.msg_there_is_no_cards_in_pack);
            return;
        }
        getViewState().requestsSelectCourseToAdd(mQPack);
    }

    public void onAddCardsToCourseCommit(Long courseId) {

        LearnCourse learnCourse = mCoursesInteractor.getCourse(courseId);
        if (mCoursesInteractor.hasMissedCards(learnCourse, mQPack.getId())) {
            getViewState().showMessage(R.string.msg_there_is_no_cards_to_learn);
            return;
        }
        mCoursesInteractor.addCardsToCourse(courseId, mCoursesInteractor.getNotInCards(learnCourse, mQPack.getId()));
        getViewState().showCourseScreen(courseId);
    }

    public void onViewPackCards(){
        getViewState().showLearnCourseCardsViewingType();
    }

    public void onViewPackCardsRandomly(){
        getViewState().showLearnCourseCards(
                mCoursesInteractor.getTempCourseFor(mQPack.getId(), true).getId());
    }

    public void onViewPackCardsOrdered(){
        getViewState().showLearnCourseCards(
                mCoursesInteractor.getTempCourseFor(mQPack.getId(), false).getId() );
    }

    public void onViewPackCardsInList(){
        getViewState().showLearnCourseCardsInList( mCoursesInteractor.getTempCourseFor(mQPack.getId(), false).getId() );
    }

    public void onUiCardsFastView() {
        List<Card> cards = mCardsInteractor.getQPackCards(mQPack);
        getViewState().setFastViewCards(cards);
    }



    // ++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++

}
