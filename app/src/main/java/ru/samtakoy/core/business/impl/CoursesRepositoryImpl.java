package ru.samtakoy.core.business.impl;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import ru.samtakoy.core.business.CoursesRepository;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.screens.log.MyLog;

public class CoursesRepositoryImpl implements CoursesRepository {

    private Context mCtx;

    public CoursesRepositoryImpl(Context ctx){
        mCtx = ctx;
    }

    public LearnCourse getCourse(Long learnCourseId){
        // TODO make cache
        return ContentProviderHelper.getConcreteCourse(mCtx, learnCourseId);
    }

    @Override
    public Observable<LearnCourse> getAllCourses() {

        return
                Observable.fromCallable(
                        () -> {
                            List<LearnCourse> result = ContentProviderHelper.getAllCourses(mCtx);
                            MyLog.add("-- courses count: "+result.size()+"m thread:"+Thread.currentThread().getName());
                            return result;
                        }
                ).flatMap(
                        courses -> Observable.fromIterable(courses)
                );
    }


}
