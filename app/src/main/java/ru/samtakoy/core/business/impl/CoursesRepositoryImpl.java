package ru.samtakoy.core.business.impl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.TempCourseRepository;
import ru.samtakoy.core.database.room.MyRoomDb;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.CourseType;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.presentation.log.MyLog;

public class CoursesRepositoryImpl implements CoursesRepository {

    private static final String TAG = "CoursesRepositoryImpl";

    @Inject
    MyRoomDb db;
    @Inject
    TempCourseRepository mTempCourseRepository;

    @Inject
    public CoursesRepositoryImpl() {
    }

    /*
    public LearnCourseEntity getCourse(Long learnCourseId) {

        if (learnCourseId == mTempCourseRepository.getTempCourseId()) {
            return mTempCourseRepository.getTempCourse();
        }
        return db.courseDao().getLearnCourse(learnCourseId);
    }*/

    @Override
    @NotNull
    public Single<LearnCourseEntity> getCourseRx(@NotNull Long learnCourseId) {


        if (learnCourseId == mTempCourseRepository.getTempCourseId()) {
            return Single.fromCallable(
                    () -> mTempCourseRepository.getTempCourse()
            );
        }

        return db.courseDao().getLearnCourseRx(learnCourseId);
    }

    @Override
    public LearnCourseEntity getTempCourseFor(Long qPackId, List<Long> cardIds, Boolean shuffleCards) {
        return mTempCourseRepository.getTempCourseFor(qPackId, cardIds, shuffleCards);
    }

    @Override
    public LearnCourseEntity getCourseByMode(LearnCourseMode mode) {
        return db.courseDao().getLearnCourseByMode(mode.getDbId());
    }

    @Override
    public boolean updateCourse(LearnCourseEntity course) {

        if (course.getCourseType() == CourseType.TEMPORARY) {
            mTempCourseRepository.updateTempCourse(course);
            return true;
        }

        return db.courseDao().updateCourse(course) > 0;
    }

    @Override
    public Completable deleteCourse(long courseId) {
        return Completable.fromCallable(() -> db.courseDao().deleteCourseById(courseId) > 0);
    }

    @Override
    public Completable deleteQPackCourses(Long qPackId) {
        return Completable.fromCallable(() -> {
            db.courseDao().deleteQPackCourses(qPackId);
            return true;
        });
    }

    @Override
    public LearnCourseEntity addNewCourseNow(LearnCourseEntity newCourse) {

        if (newCourse.getCourseType() == CourseType.TEMPORARY) {
            MyLog.add(TAG + ", CourseType.TEMPORARY cant be added to Database");
            return null;
        }

        Long id = db.courseDao().addLearnCourse(newCourse);
        newCourse.setId(id);
        return newCourse;
    }

    @Override
    public Single<LearnCourseEntity> addNewCourse(LearnCourseEntity newCourse) {
        return Single.fromCallable(
                () -> {

                    if (newCourse.getCourseType() == CourseType.TEMPORARY) {
                        String error = TAG + ", CourseType.TEMPORARY cant be added to Database";
                        MyLog.add(error);
                        throw new Exception(error);
                    }

                    Long id = db.courseDao().addLearnCourse(newCourse);
                    newCourse.setId(id);
                    return newCourse;
                }
        );
    }


    @Override
    public Flowable<List<LearnCourseEntity>> getAllCourses() {
        return db.courseDao().getAllCourses();
    }

    @Override
    public Single<List<LearnCourseEntity>> getAllCoursesSingle() {
        return db.courseDao().getAllCoursesSingle();
    }

    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesByIds(Long[] targetCourseIds) {
        return db.courseDao().getCoursesByIds(Arrays.asList(targetCourseIds));
    }

    private List<Integer> modesToIds(List<LearnCourseMode> targetModes) {
        List<Integer> result = new ArrayList<>(targetModes.size());
        for (LearnCourseMode mode : targetModes) {
            result.add(mode.getDbId());
        }
        return result;
    }

    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesByModes(List<LearnCourseMode> targetModes) {
        return db.courseDao().getCoursesByModes(modesToIds(targetModes));
    }

    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesByModes(LearnCourseMode... mode) {
        return db.courseDao().getLearnCourseByModes(Arrays.asList(mode));
    }

    @Override
    public List<LearnCourseEntity> getCoursesByModesNow(LearnCourseMode... mode) {
        return db.courseDao().getLearnCourseByModesNow(Arrays.asList(mode));
    }

    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesForQPack(Long qPackId) {
        return db.courseDao().getCoursesForQPack(qPackId);
    }

    // не в Rx стиле для сервиса, перенести в отдельный репозиторий?

    @Override
    public List<LearnCourseEntity> getOrderedCoursesLessThan(LearnCourseMode mode, Date repeatDate) {
        return db.courseDao().getOrderedCoursesLessThan(mode, repeatDate);
    }

    @Override
    public List<LearnCourseEntity> getOrderedCoursesMoreThan(LearnCourseMode mode, Date repeatDate) {
        return db.courseDao().getOrderedCoursesMoreThan(mode, repeatDate);
    }
}
