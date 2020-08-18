package ru.samtakoy.core.business.impl;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.screens.log.MyLog;

public class CoursesRepositoryImpl implements CoursesRepository {

    private Context mCtx;

    public CoursesRepositoryImpl(Context ctx) {
        mCtx = ctx;
    }

    public LearnCourse getCourse(Long learnCourseId) {
        // TODO make cache
        return ContentProviderHelper.getConcreteCourse(mCtx, learnCourseId);
    }

    @Override
    public void deleteCourse(long courseId) {
        ContentProviderHelper.deleteCourse(mCtx, courseId);
    }

    @Override
    public Single<LearnCourse> addNewCourse(LearnCourse newCourse) {
        return Single.fromCallable(
                () -> {
                    ContentProviderHelper.addNewCourse(mCtx, newCourse);
                    return newCourse;
                }
        );
    }

    @Override
    //public Observable<LearnCourse> getAllCourses() {
    public Single<List<LearnCourse>> getAllCourses() {

        return
                Observable.fromCallable(
                        () -> {
                            List<LearnCourse> result = ContentProviderHelper.getAllCourses(mCtx);
                            MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                            return result;
                        }
                ).singleOrError()
                /*.flatMap(
                        courses -> Observable.fromIterable(courses)
                )*/;
    }

    @Override
    public Single<List<LearnCourse>> getCoursesByIds(Long[] targetCourseIds) {

        return Observable.fromCallable(
                () -> {
                    List<LearnCourse> result = ContentProviderHelper.getCoursesByIds(mCtx, targetCourseIds);
                    MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                    return result;
                }
        ).singleOrError();
    }

    @Override
    public Single<List<LearnCourse>> getCoursesByModes(List<LearnCourseMode> targetModes) {

        return Observable.fromCallable(
                () -> {
                    List<LearnCourse> result = ContentProviderHelper.getCoursesByModes(mCtx, targetModes);
                    MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                    return result;
                }
        ).singleOrError();
    }

    @Override
    public Single<List<LearnCourse>> getCoursesForQPack(Long qPackId) {

        return Observable.fromCallable(
                () -> {
                    List<LearnCourse> result = ContentProviderHelper.getCoursesFor(mCtx, qPackId);
                    MyLog.add("-- courses count: " + result.size() + "m thread:" + Thread.currentThread().getName());
                    return result;
                }
        ).singleOrError();
    }
}
