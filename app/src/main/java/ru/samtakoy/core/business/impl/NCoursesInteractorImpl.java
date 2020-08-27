package ru.samtakoy.core.business.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.CoursesPlanner;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.business.QPacksRepository;
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
    public void deleteCourse(long courseId) {
        mCoursesRepository.deleteCourse(courseId);
    }

    public boolean hasMissedCards(LearnCourseEntity learnCourse, Long qPackId) {
        List<Long> cardIds = mCardsRepository.getCardsIdsFromQPack(qPackId);
        return learnCourse.hasNotInCards(cardIds);
    }

    public List<Long> getNotInCards(LearnCourseEntity learnCourse, Long qPackId) {
        List<Long> cardIds = mCardsRepository.getCardsIdsFromQPack(qPackId);
        return learnCourse.getNotInCards(cardIds);
    }

    public void addCardsToCourse(Long courseId, List<Long> newCardsToAdd) {
        LearnCourseEntity learnCourse = mCoursesRepository.getCourse(courseId);
        addCardsToCourse(learnCourse, newCardsToAdd);

    }

    public void addCardsToCourse(LearnCourseEntity learnCourse, List<Long> newCardsToAdd) {


        learnCourse.addCardsToCourse(newCardsToAdd);
        mCoursesRepository.updateCourse(learnCourse);

        // additional
        if (learnCourse.hasRealizedSchedule()) {
            Schedule addSchedule = learnCourse.getRealizedSchedule().copy();
            if (learnCourse.hasRestSchedule()) {
                addSchedule.addItem(learnCourse.getRestSchedule().getFirstItem());
            }

            String qPackTitle = null;
            if(learnCourse.hasQPackId()){
                QPackEntity qPack = mQPacksRepository.getQPack(learnCourse.getQPackId());
                qPackTitle = qPack.getTitle();
            }

            mCoursesPlanner.planAdditionalCards(learnCourse.getQPackId(), qPackTitle, newCardsToAdd, addSchedule);
        }
    }

    public Long addCourseForQPack(String courseTitle, Long qPackId) {
        List<Long> cardIds = mCardsRepository.getCardsIdsFromQPack(qPackId);
        Schedule schedule = Schedule.DEFAULT;

        LearnCourseEntity course = LearnCourseEntity.Companion.initNew(
                qPackId, courseTitle, LearnCourseMode.PREPARING,
                cardIds, schedule, null
        );
        return mCoursesRepository.addNewCourseNow(course);
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

    public void saveCourse(LearnCourseEntity learnCourse) {
        mCoursesRepository.updateCourse(learnCourse);
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

    @Override
    public LearnCourseEntity getTempCourseFor(Long qPackId, boolean shuffleCards) {
        List<Long> cardIds = mCardsRepository.getCardsIdsFromQPack(qPackId);
        return getTempCourseFor(qPackId, cardIds, shuffleCards);
    }

}
