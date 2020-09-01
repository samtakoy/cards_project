package ru.samtakoy.core.business;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;

public interface NCoursesInteractor {

    //TODO  -> RxJava
    LearnCourseEntity getCourse(Long courseId);

    //TODO  -> RxJava
    void deleteCourse(Long courseId);

    Completable deleteQPackCourses(Long qPackId);

    Completable onAddCardsToCourseFromQPack(Long qPackId, Long learnCourseId);

    Completable addCardsToCourseRx(LearnCourseEntity learnCourse, List<Long> newCardsToAdd);

    Single<LearnCourseEntity> addCourseForQPack(String courseTitle, Long qPackId);

    //TODO  -> RxJava
    boolean saveCourse(LearnCourseEntity learnCourse);

    Single<LearnCourseEntity> addNewCourse(@Nullable LearnCourseEntity newCourse);

    @NotNull
    Flowable<List<LearnCourseEntity>> getAllCourses();

    @NotNull
    Flowable<List<LearnCourseEntity>> getCoursesByIds(@NotNull Long[] targetCourseIds);

    @NotNull
    Flowable<List<LearnCourseEntity>> getCoursesByModes(@NotNull List<LearnCourseMode> targetModes);

    @NotNull
    Flowable<List<LearnCourseEntity>> getCoursesForQPack(@NotNull Long qPackId);

    //TODO  -> RxJava
    LearnCourseEntity getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards);

    @NotNull
    Single<LearnCourseEntity> getTempCourseFor_rx(Long qPackId, boolean shuffleCards);


}
