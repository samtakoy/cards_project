package ru.samtakoy.core.business;

import java.util.List;

import io.reactivex.Single;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;

public interface CoursesRepository {

    LearnCourse getCourse(Long learnCourseId);

    void deleteCourse(long courseId);

    Single<LearnCourse> addNewCourse(LearnCourse newCourse);

    //Observable<LearnCourse> getAllCourses();
    Single<List<LearnCourse>> getAllCourses();

    Single<List<LearnCourse>> getCoursesByIds(Long[] targetCourseIds);

    Single<List<LearnCourse>> getCoursesByModes(List<LearnCourseMode> targetModes);

    Single<List<LearnCourse>> getCoursesForQPack(Long qPackId);


}
