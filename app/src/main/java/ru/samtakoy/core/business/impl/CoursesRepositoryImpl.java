package ru.samtakoy.core.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.business.TempCourseRepository;
import ru.samtakoy.core.database.room.MyRoomDb;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.CourseType;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.screens.log.MyLog;

public class CoursesRepositoryImpl implements CoursesRepository {

    private static final String TAG = "CoursesRepositoryImpl";

    @Inject
    MyRoomDb db;
    @Inject
    TempCourseRepository mTempCourseRepository;

    @Inject
    public CoursesRepositoryImpl() {
    }

    public LearnCourseEntity getCourse(Long learnCourseId) {

        if (learnCourseId == mTempCourseRepository.getTempCourseId()) {
            return mTempCourseRepository.getTempCourse();
        }
        return db.courseDao().getLearnCourse(learnCourseId);
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
    public boolean deleteCourse(long courseId) {
        return db.courseDao().deleteCourseById(courseId) > 0;
    }

    @Override
    public Long addNewCourseNow(LearnCourseEntity newCourse) {

        if (newCourse.getCourseType() == CourseType.TEMPORARY) {
            MyLog.add(TAG + ", CourseType.TEMPORARY cant be added to Database");
            return 0L;
        }

        Long id = db.courseDao().addLearnCourse(newCourse);
        newCourse.setId(id);
        return id;
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
    public Single<List<LearnCourseEntity>> getAllCourses() {
        return db.courseDao().getAllCourses().singleOrError();
    }

    @Override
    public Single<List<LearnCourseEntity>> getCoursesByIds(Long[] targetCourseIds) {
        return db.courseDao().getCoursesByIds(Arrays.asList(targetCourseIds)).singleOrError();
    }

    private List<Integer> modesToIds(List<LearnCourseMode> targetModes) {
        List<Integer> result = new ArrayList<>(targetModes.size());
        for (LearnCourseMode mode : targetModes) {
            result.add(mode.getDbId());
        }
        return result;
    }

    @Override
    public Single<List<LearnCourseEntity>> getCoursesByModes(List<LearnCourseMode> targetModes) {
        return db.courseDao().getCoursesByModes(modesToIds(targetModes)).singleOrError();
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
    public Single<List<LearnCourseEntity>> getCoursesForQPack(Long qPackId) {
        return db.courseDao().getCoursesForQPack(qPackId).singleOrError();
    }

    @Override
    public List<LearnCourseEntity> getCoursesLessThan(LearnCourseMode mode, Date repeatDate) {
        return db.courseDao().getCoursesLessThan(mode, repeatDate);
    }

    @Override
    public List<LearnCourseEntity> getCoursesMoreThan(LearnCourseMode mode, Date repeatDate) {
        return db.courseDao().getCoursesMoreThan(mode, repeatDate);
    }
}
