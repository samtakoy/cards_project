package ru.samtakoy.core.presentation.qpack;


import java.util.List;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.database.room.entities.CardEntity;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.QPackEntity;

@InjectViewState
public class QPackInfoPresenter extends MvpPresenter<QPackInfoView> {

    private QPackEntity mQPack;

    public CardsInteractor mCardsInteractor;
    public NCoursesInteractor mCoursesInteractor;

    public static class Factory {

        @Inject
        public CardsInteractor mCardsInteractor;
        @Inject
        public NCoursesInteractor mCoursesInteractor;

        @Inject
        Factory() {
        }

        public QPackInfoPresenter create(Long qPackId) {
            return new QPackInfoPresenter(
                    mCardsInteractor, mCoursesInteractor, qPackId
            );
        }
    }


    public QPackInfoPresenter(
            CardsInteractor cardsInteractor,
            NCoursesInteractor coursesInteractor,
            Long qPackId
    ) {

        mCardsInteractor = cardsInteractor;
        mCoursesInteractor = coursesInteractor;

        setup(qPackId);
    }

    private QPackInfoPresenter setup(Long qPackId) {
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
        getViewState().showCourses(mQPack.getId());
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
        getViewState().requestsSelectCourseToAdd(mQPack.getId());
    }

    public void onAddCardsToCourseCommit(Long courseId) {

        LearnCourseEntity learnCourse = mCoursesInteractor.getCourse(courseId);
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
        List<CardEntity> cards = mCardsInteractor.getQPackCards(mQPack.getId());
        getViewState().setFastViewCards(cards);
    }



    // ++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++

}
