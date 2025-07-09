package ru.samtakoy.core.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode;

public interface NCoursesInteractor {

    @NotNull
    Single<LearnCourseEntity> getCourse(@NotNull Long courseId);

    @NotNull
    Completable deleteCourse(@NotNull Long courseId);

    @NotNull
    Completable deleteQPackCourses(@NotNull Long qPackId);

    @NotNull
    Completable onAddCardsToCourseFromQPack(@NotNull Long qPackId, @NotNull Long learnCourseId);

    @NotNull
    Completable addCardsToCourseRx(@NotNull LearnCourseEntity learnCourse, @NotNull List<Long> newCardsToAdd);

    @NotNull
    Single<LearnCourseEntity> addCourseForQPack(String courseTitle, @NotNull Long qPackId);

    @NotNull
    Completable saveCourse(@NotNull LearnCourseEntity learnCourse);

    @NotNull
    Single<LearnCourseEntity> addNewCourse(@NotNull LearnCourseEntity newCourse);

    @NotNull
    Flowable<List<LearnCourseEntity>> getAllCourses();

    @NotNull
    Flowable<List<LearnCourseEntity>> getCoursesByIds(@NotNull Long[] targetCourseIds);

    @NotNull
    Flowable<List<LearnCourseEntity>> getCoursesByModes(@NotNull List<LearnCourseMode> targetModes);

    @NotNull
    Flowable<List<LearnCourseEntity>> getCoursesForQPack(@NotNull Long qPackId);

    @NotNull
    Single<LearnCourseEntity> getTempCourseFor(@NotNull Long qPackId, @NotNull List<Long> cardIds, boolean shuffleCards);

    @NotNull
    Single<LearnCourseEntity> getTempCourseFor_rx(@NotNull Long qPackId, boolean shuffleCards);

    @NotNull
    Completable finishCourseCardsViewing(@NotNull LearnCourseEntity course, @NotNull Date currentTime);
}
