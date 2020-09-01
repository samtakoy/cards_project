package ru.samtakoy.core.presentation.qpack;


import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.business.utils.MessageException;
import ru.samtakoy.core.database.room.entities.other.QPackWithCardIds;
import ru.samtakoy.core.presentation.log.MyLog;

import static ru.samtakoy.core.business.utils.TransformersKt.c_io_mainThread;
import static ru.samtakoy.core.business.utils.TransformersKt.f_io_mainThread;
import static ru.samtakoy.core.business.utils.TransformersKt.s_io_mainThread;

@InjectViewState
public class QPackInfoPresenter extends MvpPresenter<QPackInfoView> {

    private QPackWithCardIds mQPack;
    //private int mQPackCardsCount = 0;

    public CardsInteractor mCardsInteractor;
    public NCoursesInteractor mCoursesInteractor;

    private Disposable mQPackSubscribtion;
    private Disposable mFastCardsSubscribtion;
    private CompositeDisposable mLastOptSubscribtion = new CompositeDisposable();


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

        bindData(qPackId);
    }

    @Override
    public void onDestroy() {

        if (mFastCardsSubscribtion != null) {
            mFastCardsSubscribtion.dispose();
        }
        if (mQPackSubscribtion != null) {
            mQPackSubscribtion.dispose();
        }
        mLastOptSubscribtion.dispose();

        super.onDestroy();
    }

    private void bindData(Long qPackId) {

        mQPackSubscribtion = mCardsInteractor.getQPackWithCardIds(qPackId)
                .compose(f_io_mainThread())
                .subscribe(
                        qPackWithCardIds -> {
                            mQPack = qPackWithCardIds;
                            getViewState().initView(mQPack.getQPack().getTitle(), String.valueOf(mQPack.getCardCount()));
                        }
                );
    }

    private boolean hasPackCards() {
        return mQPack != null && mQPack.getCardCount() > 0;
    }


    public void onUiDeletePack() {

        // TODO вынести куда-то контроль комплексного удаления
        runOpt(
                mCoursesInteractor.deleteQPackCourses(mQPack.getId())
                        .andThen(mCardsInteractor.deleteQPack(mQPack.getId()))
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> getViewState().closeScreen(),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onUiNewCourseCommit(String courseTitle) {
        runOpt(
                mCoursesInteractor.addCourseForQPack(courseTitle, mQPack.getId())
                        .compose(s_io_mainThread())
                        .subscribe(
                                courseEntity -> onCourseAdded(courseEntity.getId()),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onCourseAdded(Long courseId) {
        getViewState().showCourseScreen(courseId);
    }

    public void onUiShowPackCourses() {
        getViewState().navigateToPackCourses(mQPack.getId());
    }

    public void onUiAddToNewCourse() {
        if (!hasPackCards()) {
            getViewState().showMessage(R.string.msg_there_is_no_cards_in_pack);
            return;
        }
        getViewState().requestNewCourseCreation(mQPack.getQPack().getTitle());
    }

    public void onUiAddToExistsCourse() {
        if (!hasPackCards()) {
            getViewState().showMessage(R.string.msg_there_is_no_cards_in_pack);
            return;
        }
        getViewState().requestsSelectCourseToAdd(mQPack.getId());
    }

    public void onUiAddCardsToCourseCommit(Long courseId) {

        runOpt(
                mCoursesInteractor.onAddCardsToCourseFromQPack(mQPack.getId(), courseId)
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> onCardsToCourseAdded(courseId),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onCardsToCourseAdded(Long courseId) {
        getViewState().showCourseScreen(courseId);
    }

    private void runOpt(@NonNull Disposable disposable) {
        mLastOptSubscribtion.clear();
        mLastOptSubscribtion.add(disposable);
    }

    private void onGetError(Throwable t) {

        if (t instanceof MessageException) {
            getViewState().showMessage(((MessageException) t).getMsgId());
        } else {
            MyLog.add(ExceptionUtils.getMessage(t), t);
            getViewState().showMessage(R.string.db_request_err_message);
        }
    }

    public void onUiViewPackCards() {
        getViewState().showLearnCourseCardsViewingType();
    }

    private void onViewPackCards(boolean shuffleCards) {
        runOpt(
                mCoursesInteractor.getTempCourseFor_rx(mQPack.getId(), shuffleCards)
                        .compose(s_io_mainThread())
                        .subscribe(
                                learnCourseEntity -> getViewState().navigateToCardsView(learnCourseEntity.getId()),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onUiViewPackCardsRandomly() {
        onViewPackCards(true);
    }

    public void onUiViewPackCardsOrdered() {
        onViewPackCards(false);
    }

    public void onUiViewPackCardsInList() {
        runOpt(
                mCoursesInteractor.getTempCourseFor_rx(mQPack.getId(), false)
                        .compose(s_io_mainThread())
                        .subscribe(
                                learnCourseEntity -> getViewState().openLearnCourseCardsInList(learnCourseEntity.getId()),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    public void onUiCardsFastView() {

        if (mFastCardsSubscribtion == null) {

            mFastCardsSubscribtion = mCardsInteractor.getQPackCards(mQPack.getId())
                    .compose(f_io_mainThread())
                    .subscribe(
                            cardEntities -> {
                                getViewState().setFastViewCards(cardEntities);
                            }
                    );
        }
    }

    public void onUiAddFakeCard() {
        runOpt(
                mCardsInteractor.addFakeCard(mQPack.getId())
                        .compose(c_io_mainThread())
                        .subscribe(
                                () -> getViewState().showMessage(R.string.btn_ok),
                                throwable -> onGetError(throwable)
                        )
        );
    }


}
