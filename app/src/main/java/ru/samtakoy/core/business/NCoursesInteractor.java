package ru.samtakoy.core.business;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Single;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;

public interface NCoursesInteractor {

    LearnCourse getCourse(Long courseId);

    void deleteCourse(long courseId);

    boolean hasMissedCards(LearnCourse learnCourse, Long qPackId);

    List<Long> getNotInCards(LearnCourse learnCourse, Long qPackId);

    void addCardsToCourse(Long courseId, List<Long> newCardsToAdd);

    void addCardsToCourse(LearnCourse learnCourse, List<Long> newCardsToAdd);

    Long addCourseForQPack(String courseTitle, Long qPackId);

    void saveCourse(LearnCourse learnCourse);

    Single<LearnCourse> addNewCourse(@Nullable LearnCourse newCourse);

    @NotNull
    Single<List<LearnCourse>> getAllCourses();

    @NotNull
    Single<List<LearnCourse>> getCoursesByIds(@NotNull Long[] targetCourseIds);

    @NotNull
    Single<List<LearnCourse>> getCoursesByModes(@NotNull List<LearnCourseMode> targetModes);

    @NotNull
    Single<List<LearnCourse>> getCoursesForQPack(@NotNull Long qPackId);

    LearnCourse getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards);

    LearnCourse getTempCourseFor(Long qPackId, boolean shuffleCards);


}
