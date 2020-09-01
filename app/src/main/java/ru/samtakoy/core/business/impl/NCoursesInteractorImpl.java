package ru.samtakoy.core.business.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.business.QPacksRepository;
import ru.samtakoy.core.business.utils.LearnCourseCardsIdsPair;
import ru.samtakoy.core.business.utils.MessageException;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.elements.Schedule;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;

public class NCoursesInteractorImpl implements NCoursesInteractor {

    @Inject
    CardsRepository mCardsRepository;
    @Inject
    QPacksRepository mQPacksRepository;
    @Inject
    CoursesRepository mCoursesRepository;
    @Inject
    CoursesPlanner mCoursesPlanner;

    private static final Date SOME_DATE = new Date(0);

    @Inject
    public NCoursesInteractorImpl() {
    }
            /*Context ctx,
            QPacksRepository cardsRepository,
            CoursesRepository corsesRepository
    ) {
        mCtx = ctx;
        mQPacksRepository = cardsRepository;
        mCoursesRepository = corsesRepository;
    }*/

    public LearnCourseEntity getCourse(Long courseId) {
        return mCoursesRepository.getCourse(courseId);
    }

    @Override
    public void deleteCourse(Long courseId) {
        mCoursesRepository.deleteCourse(courseId);
    }

    @Override
    public Completable deleteQPackCourses(Long qPackId) {
        return mCoursesRepository.deleteQPackCourses(qPackId);
    }

    @Override
    public Completable onAddCardsToCourseFromQPack(Long qPackId, Long learnCourseId) {
        return
                Single.zip(
                        mCoursesRepository.getCourseRx(learnCourseId),
                        mCardsRepository.getCardsIdsFromQPack(qPackId),
                        (learnCourse, cardIds) -> new LearnCourseCardsIdsPair(learnCourse, cardIds)
                )
                        .map(courseCardsIdsPair -> validateAddingCardsToCourse(courseCardsIdsPair))
                        .flatMapCompletable(
                                courseCardsIdsPair ->
                                        addCardsToCourseRx(courseCardsIdsPair.getLearnCourse(), courseCardsIdsPair.getNotInCards())
                        );
    }

    private LearnCourseCardsIdsPair validateAddingCardsToCourse(LearnCourseCardsIdsPair learnCourseCardsIdsPair) throws MessageException {
        if (!learnCourseCardsIdsPair.hasNotInCards()) {
            throw new MessageException(R.string.msg_there_is_no_new_cards_to_learn);
        }
        return learnCourseCardsIdsPair;
    }

    public Completable addCardsToCourseRx(LearnCourseEntity learnCourse, List<Long> newCardsToAdd) {
        return Completable.fromCallable(() -> {
            addCardsToCourseOld(learnCourse, newCardsToAdd);
            return true;
        });
    }

    // TODO refactor
    private void addCardsToCourseOld(LearnCourseEntity learnCourse, List<Long> newCardsToAdd) {

        // TODO проверить - происходит ли добавление, 2) добавляется ли в список активного курса (в процессе повтороения)

        learnCourse.addCardsToCourse(newCardsToAdd);
        mCoursesRepository.updateCourse(learnCourse);

        //
        // additional
        // TODO надо спрашивать пользователя отдельно после добавления карточек курс
        if (learnCourse.hasRealizedSchedule()) {

            Schedule addSchedule = learnCourse.getRealizedSchedule().copy();
            if (learnCourse.hasRestSchedule()) {
                addSchedule.addItem(learnCourse.getRestSchedule().getFirstItem());
            }

            String qPackTitle = null;
            if (learnCourse.hasQPackId()) {
                QPackEntity qPack = mQPacksRepository.getQPack(learnCourse.getQPackId());
                qPackTitle = qPack.getTitle();
            }

            mCoursesPlanner.planAdditionalCards(learnCourse.getQPackId(), qPackTitle, newCardsToAdd, addSchedule);
        }
    }

    @Override
    public Single<LearnCourseEntity> addCourseForQPack(String courseTitle, Long qPackId) {
        return mCardsRepository.getCardsIdsFromQPack(qPackId)
                .flatMap(cardIds -> addCourseForQPack(courseTitle, qPackId, cardIds));
    }

    private Single<LearnCourseEntity> addCourseForQPack(String courseTitle, Long qPackId, List<Long> cardIds) {

        Schedule schedule = Schedule.DEFAULT;

        LearnCourseEntity course = LearnCourseEntity.Companion.initNew(
                qPackId, courseTitle, LearnCourseMode.PREPARING,
                cardIds, schedule, null
        );
        return mCoursesRepository.addNewCourse(course);
    }

    @NotNull
    @Override
    public Flowable<List<LearnCourseEntity>> getAllCourses() {
        return mCoursesRepository.getAllCourses();
    }

    @NotNull
    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesByIds(@NotNull Long[] targetCourseIds) {
        return mCoursesRepository.getCoursesByIds(targetCourseIds);
    }

    @NotNull
    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesByModes(@NotNull List<LearnCourseMode> targetModes) {
        return mCoursesRepository.getCoursesByModes(targetModes);
    }

    @NotNull
    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesForQPack(@NotNull Long qPackId) {
        return mCoursesRepository.getCoursesForQPack(qPackId);
    }

    public boolean saveCourse(LearnCourseEntity learnCourse) {
        return mCoursesRepository.updateCourse(learnCourse);
    }

    @Override
    public Single<LearnCourseEntity> addNewCourse(@Nullable LearnCourseEntity newCourse) {
        return mCoursesRepository.addNewCourse(newCourse);
    }

    // ---

    @Override
    public LearnCourseEntity getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards) {
        return mCoursesRepository.getTempCourseFor(qPackId, cardIds, shuffleCards);
    }

    @NotNull
    @Override
    public Single<LearnCourseEntity> getTempCourseFor_rx(Long qPackId, boolean shuffleCards) {
        return mCardsRepository
                .getCardsIdsFromQPack(qPackId)
                .flatMap(
                        cardIds ->
                                Single.fromCallable(() -> getTempCourseFor(qPackId, cardIds, shuffleCards))
                );
    }

}
