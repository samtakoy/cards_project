package ru.samtakoy.core.business;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;

public interface NCoursesInteractor {

    LearnCourseEntity getCourse(Long courseId);

    void deleteCourse(long courseId);

    boolean hasMissedCards(LearnCourseEntity learnCourse, Long qPackId);

    List<Long> getNotInCards(LearnCourseEntity learnCourse, Long qPackId);

    void addCardsToCourse(Long courseId, List<Long> newCardsToAdd);

    void addCardsToCourse(LearnCourseEntity learnCourse, List<Long> newCardsToAdd);

    Long addCourseForQPack(String courseTitle, Long qPackId);

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

    LearnCourseEntity getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards);

    LearnCourseEntity getTempCourseFor(Long qPackId, boolean shuffleCards);


}
