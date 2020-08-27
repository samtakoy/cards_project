package ru.samtakoy.core.business;


import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;

public interface CoursesRepository {

    @Nullable
    LearnCourseEntity getCourse(@NotNull Long learnCourseId);

    @NotNull
    LearnCourseEntity getTempCourseFor(Long qPackId, List<Long> cardIds, Boolean shuffleCards);

    @Nullable
    LearnCourseEntity getCourseByMode(@NotNull LearnCourseMode mode);


    boolean updateCourse(LearnCourseEntity course);

    boolean deleteCourse(long courseId);

    // TODO TEMP
    Long addNewCourseNow(LearnCourseEntity newCourse);

    // TODO Single вернуть к Flowable, для возможности update данных

    Single<LearnCourseEntity> addNewCourse(LearnCourseEntity newCourse);

    Flowable<List<LearnCourseEntity>> getAllCourses();

    Single<List<LearnCourseEntity>> getAllCoursesSingle();

    Flowable<List<LearnCourseEntity>> getCoursesByIds(Long[] targetCourseIds);

    Flowable<List<LearnCourseEntity>> getCoursesByModes(List<LearnCourseMode> targetModes);

    Flowable<List<LearnCourseEntity>> getCoursesByModes(LearnCourseMode... mode);

    List<LearnCourseEntity> getCoursesByModesNow(LearnCourseMode... mode);

    Flowable<List<LearnCourseEntity>> getCoursesForQPack(Long qPackId);


    List<LearnCourseEntity> getOrderedCoursesLessThan(LearnCourseMode mode, Date repeatDate);

    List<LearnCourseEntity> getOrderedCoursesMoreThan(LearnCourseMode mode, Date repeatDate);

}
