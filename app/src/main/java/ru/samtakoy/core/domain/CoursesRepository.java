package ru.samtakoy.core.domain;


import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode;

public interface CoursesRepository {

    @NotNull
    Single<LearnCourseEntity> getCourseRx(@NotNull Long learnCourseId);

    @NotNull
    LearnCourseEntity getTempCourseFor(Long qPackId, List<Long> cardIds, Boolean shuffleCards);

    @Nullable
    LearnCourseEntity getCourseByMode(@NotNull LearnCourseMode mode);


    boolean updateCourse(LearnCourseEntity course);

    Completable deleteCourse(long courseId);

    Completable deleteQPackCourses(Long qPackId);

    // TODO TEMP
    LearnCourseEntity addNewCourseNow(LearnCourseEntity newCourse);

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
