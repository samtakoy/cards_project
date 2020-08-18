package ru.samtakoy.core.business.impl;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.List;

import io.reactivex.Single;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.NCoursesInteractor;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.elements.Schedule;

public class NCoursesInteractorImpl implements NCoursesInteractor {

    // TODO  вместо этого ContentResolver?
    private Context mCtx;
    private CardsRepository mCardsRepository;
    private CoursesRepository mCoursesRepository;

    private static final Date SOME_DATE = new Date(0);

    public NCoursesInteractorImpl(
            Context ctx,
            CardsRepository cardsRepository,
            CoursesRepository corsesRepository
    ) {
        mCtx = ctx;
        mCardsRepository = cardsRepository;
        mCoursesRepository = corsesRepository;
    }

    public LearnCourse getCourse(Long courseId) {
        return mCoursesRepository.getCourse(courseId);
    }

    @Override
    public void deleteCourse(long courseId) {
        mCoursesRepository.deleteCourse(courseId);
    }

    public boolean hasMissedCards(LearnCourse learnCourse, Long qPackId) {
        List<Long> cardIds = ContentProviderHelper.getQPackCardIdsAsList(mCtx.getContentResolver(), qPackId);
        //LearnCourse learnCourse = mCoursesRepository.getCourse(courseId);
        return learnCourse.hasNotInCards(cardIds);
    }

    public List<Long> getNotInCards(LearnCourse learnCourse, Long qPackId) {
        List<Long> cardIds = ContentProviderHelper.getQPackCardIdsAsList(mCtx.getContentResolver(), qPackId);
        //LearnCourse learnCourse = mCoursesRepository.getCourse(courseId);
        return learnCourse.getNotInCards(cardIds);
    }

    public void addCardsToCourse(Long courseId, List<Long> newCardsToAdd) {
        LearnCourse learnCourse = mCoursesRepository.getCourse(courseId);
        addCardsToCourse(learnCourse, newCardsToAdd);

    }
    public void addCardsToCourse(LearnCourse learnCourse, List<Long> newCardsToAdd) {


        learnCourse.addCardsToCourse(newCardsToAdd);
        ContentProviderHelper.saveCourse(mCtx, learnCourse);

        // additional
        if(learnCourse.hasRealizedSchedule()){
            Schedule addSchedule = learnCourse.getRealizedSchedule().copy();
            if(learnCourse.hasRestSchedule()){
                addSchedule.addItem(learnCourse.getRestSchedule().getFirstItem());
            }

            String qPackTitle = null;
            if(learnCourse.hasQPackId()){
                QPack qPack = mCardsRepository.getQPack(learnCourse.getQPackId());
                qPackTitle = qPack.getTitle();
            }

            LearnCourseHelper.planAdditionalCards(
                    mCtx, learnCourse.getQPackId(), qPackTitle, newCardsToAdd, addSchedule
            );
        }
    }

    public Long addCourseForQPack(String courseTitle, Long qPackId) {
        List<Long> cardIds = ContentProviderHelper.getQPackCardIdsAsList(mCtx.getContentResolver(), qPackId);
        Schedule schedule = Schedule.DEFAULT;
        return LearnCourseHelper.addNewCourse(mCtx, qPackId, courseTitle, cardIds, schedule, null);
    }

    @NotNull
    @Override
    public Single<List<LearnCourse>> getAllCourses() {
        return mCoursesRepository.getAllCourses();
    }

    @NotNull
    @Override
    public Single<List<LearnCourse>> getCoursesByIds(@NotNull Long[] targetCourseIds) {
        return mCoursesRepository.getCoursesByIds(targetCourseIds);
    }

    @NotNull
    @Override
    public Single<List<LearnCourse>> getCoursesByModes(@NotNull List<LearnCourseMode> targetModes) {
        return mCoursesRepository.getCoursesByModes(targetModes);
    }

    @NotNull
    @Override
    public Single<List<LearnCourse>> getCoursesForQPack(@NotNull Long qPackId) {
        return mCoursesRepository.getCoursesForQPack(qPackId);
    }

    public void saveCourse(LearnCourse learnCourse) {
        ContentProviderHelper.saveCourse(mCtx, learnCourse);
    }

    @Override
    public Single<LearnCourse> addNewCourse(@Nullable LearnCourse newCourse) {
        return mCoursesRepository.addNewCourse(newCourse);
    }

    // ---

    @Override
    public LearnCourse getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards) {
        return ContentProviderHelper.getTempCourseFor(mCtx, qPackId, cardIds, shuffleCards);
    }

    @Override
    public LearnCourse getTempCourseFor(Long qPackId, boolean shuffleCards) {
        List<Long> cardIds = ContentProviderHelper.getQPackCardIdsAsList(mCtx.getContentResolver(), qPackId);
        return getTempCourseFor(qPackId, cardIds, shuffleCards);
    }

}
