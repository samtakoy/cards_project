package ru.samtakoy.core.business;

import io.reactivex.Observable;
import ru.samtakoy.core.model.LearnCourse;

public interface CoursesRepository {

    LearnCourse getCourse(Long learnCourseId);

    Observable<LearnCourse> getAllCourses();
}
