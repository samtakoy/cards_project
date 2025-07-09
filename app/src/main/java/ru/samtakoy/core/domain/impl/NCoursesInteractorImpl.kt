package ru.samtakoy.core.domain.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.R;
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule;
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType;
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.domain.CardsRepository;
import ru.samtakoy.core.domain.CoursesPlanner;
import ru.samtakoy.core.domain.CoursesRepository;
import ru.samtakoy.core.domain.NCoursesInteractor;
import ru.samtakoy.core.domain.QPacksRepository;
import ru.samtakoy.core.domain.utils.LearnCourseCardsIdsPair;
import ru.samtakoy.core.domain.utils.MessageException;

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

    @NotNull
    @Override
    public Single<LearnCourseEntity> getCourse(Long courseId) {
        return mCoursesRepository.getCourseRx(courseId);
    }

    @NotNull
    @Override
    public Completable deleteCourse(Long courseId) {
        return mCoursesRepository.deleteCourse(courseId);
    }

    @NotNull
    @Override
    public Completable deleteQPackCourses(Long qPackId) {
        return mCoursesRepository.deleteQPackCourses(qPackId);
    }

    @NotNull
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

    @NotNull
    @Override
    public Completable addCardsToCourseRx(LearnCourseEntity learnCourse, List<Long> newCardsToAdd) {
        return Completable.fromCallable(() -> {

            // TODO проверить - происходит ли добавление, 2) добавляется ли в список активного курса (в процессе повтороения)
            learnCourse.addCardsToCourse(newCardsToAdd);
            mCoursesRepository.updateCourse(learnCourse);
            return true;
        }).andThen(
                // additional
                // TODO надо спрашивать пользователя отдельно после добавления карточек курс
                planAdditionalCourseAfterCardsAdding(learnCourse, newCardsToAdd)
        );
    }

    @NotNull
    private Completable planAdditionalCourseAfterCardsAdding(LearnCourseEntity learnCourse, List<Long> newCardsToAdd) {
        return Completable.fromCallable(
                () -> {
                    if (learnCourse.hasRealizedSchedule()) {

                        Schedule addSchedule = learnCourse.getRealizedSchedule().copy();
                        if (learnCourse.hasRestSchedule()) {
                            addSchedule.addItem(learnCourse.getRestSchedule().getFirstItem());
                        }
                        mCoursesPlanner.planAdditionalCards(learnCourse.getQPackId(), learnCourse.getTitle() + "+", newCardsToAdd, addSchedule);
                    }
                    return true;
                }
        );
    }

    @NotNull
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

    @NotNull
    @Override
    public Completable saveCourse(LearnCourseEntity learnCourse) {
        return Completable.fromCallable(() -> mCoursesRepository.updateCourse(learnCourse));
    }

    @NotNull
    @Override
    public Single<LearnCourseEntity> addNewCourse(@NotNull LearnCourseEntity newCourse) {
        return mCoursesRepository.addNewCourse(newCourse);
    }

    // ---

    @NotNull
    @Override
    public Single<LearnCourseEntity> getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards) {
        return Single.fromCallable(
                () -> mCoursesRepository.getTempCourseFor(qPackId, cardIds, shuffleCards)
        );
    }

    @NotNull
    @Override
    public Single<LearnCourseEntity> getTempCourseFor_rx(Long qPackId, boolean shuffleCards) {
        return mCardsRepository
                .getCardsIdsFromQPack(qPackId)
                .flatMap(cardIds -> getTempCourseFor(qPackId, cardIds, shuffleCards));
    }

    @NotNull
    @Override
    public Completable finishCourseCardsViewing(LearnCourseEntity course, Date currentTime) {
        return Completable.fromCallable(
                () -> {
                    course.finishLearnOrRepeat(currentTime);
                    if (course.getCourseType() != CourseType.TEMPORARY) {
                        mQPacksRepository.updateQPackViewCount(course.getQPackId(), currentTime);
                    }
                    return true;
                }
        )
                .andThen(saveCourse(course))
                .andThen(Completable.fromCallable(
                        () -> {
                            // перепланировать следующий курс
                            if (course.getCourseType() != CourseType.TEMPORARY) {
                                mCoursesPlanner.reScheduleLearnCourses();
                            }
                            return true;
                        }
                ));
    }
}
