package ru.samtakoy.core.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.database.room.MyRoomDb;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;

public class CoursesRepositoryImpl implements CoursesRepository {

    private MyRoomDb db;

    public CoursesRepositoryImpl(MyRoomDb db) {
        this.db = db;
    }

    public LearnCourseEntity getCourse(Long learnCourseId) {
        return db.courseDao().getLearnCourse(learnCourseId);
    }

    @Override
    public LearnCourseEntity getCourseByMode(LearnCourseMode mode) {
        return db.courseDao().getLearnCourseByMode(mode.getId());
    }


    @Override
    public void updateCourse(LearnCourseEntity course) {
        db.courseDao().updateCourse(course);
    }

    @Override
    public void deleteCourse(long courseId) {
        db.courseDao().deleteCourseById(courseId);
    }

    @Override
    public Long addNewCourseNow(LearnCourseEntity newCourse) {
        Long id = db.courseDao().addLearnCourse(newCourse);
        newCourse.setId(id);
        return id;
    }

    @Override
    public Single<LearnCourseEntity> addNewCourse(LearnCourseEntity newCourse) {
        return Single.fromCallable(
                () -> {
                    Long id = db.courseDao().addLearnCourse(newCourse);
                    newCourse.setId(id);
                    return newCourse;
                }
        );
    }

    @Override
    //public Observable<LearnCourse> getAllCourses() {
    public Single<List<LearnCourseEntity>> getAllCourses() {
        return db.courseDao().getAllCoursesExcept(LearnCourseMode.TEMPORARY.getId()).singleOrError();
        /*
        return
                Observable.fromCallable(
                        () -> {
                            List<LearnCourseEntity> result = ContentProviderHelper.getAllCourses(mCtx);
                            MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                            return result;
                        }
                ).singleOrError();*/
    }

    @Override
    public Single<List<LearnCourseEntity>> getCoursesByIds(Long[] targetCourseIds) {
        return db.courseDao().getCoursesByIds(Arrays.asList(targetCourseIds)).singleOrError();
        /*return Observable.fromCallable(
                () -> {
                    List<LearnCourseEntity> result = ContentProviderHelper.getCoursesByIds(mCtx, targetCourseIds);
                    MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                    return result;
                }
        ).singleOrError();*/
    }

    private List<Integer> modesToIds(List<LearnCourseMode> targetModes) {
        List<Integer> result = new ArrayList<>(targetModes.size());
        for (LearnCourseMode mode : targetModes) {
            result.add(mode.getId());
        }
        return result;
    }

    @Override
    public Single<List<LearnCourseEntity>> getCoursesByModes(List<LearnCourseMode> targetModes) {

        return db.courseDao().getCoursesByModes(modesToIds(targetModes)).singleOrError();
        /*return Observable.fromCallable(
                () -> {
                    List<LearnCourseEntity> result = ContentProviderHelper.getCoursesByModes(mCtx, targetModes);
                    MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                    return result;
                }
        ).singleOrError();*/
    }

    @Override
    public Flowable<List<LearnCourseEntity>> getCoursesByModes(LearnCourseMode... mode) {

        /*
        List<Integer> modes = new ArrayList<>(mode.length);
        for(LearnCourseMode oneMode: mode){
            modes.add(oneMode.getId());
        }*/
        return db.courseDao().getLearnCourseByModes(Arrays.asList(mode));
    }

    @Override
    public List<LearnCourseEntity> getCoursesByModesNow(LearnCourseMode... mode) {
        return db.courseDao().getLearnCourseByModesNow(Arrays.asList(mode));
    }

    @Override
    public Single<List<LearnCourseEntity>> getCoursesForQPack(Long qPackId) {

        return db.courseDao().getCoursesForQPack(qPackId).singleOrError();
        /*return Observable.fromCallable(
                () -> {
                    List<LearnCourseEntity> result = ContentProviderHelper.getCoursesFor(mCtx, qPackId);
                    MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                    return result;
                }
        ).singleOrError();*/
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
