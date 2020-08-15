package ru.samtakoy.core.business;

import java.util.List;

import ru.samtakoy.core.model.LearnCourse;

public interface NCoursesInteractor {

    LearnCourse getCourse(Long courseId);
    boolean hasMissedCards(LearnCourse learnCourse, Long qPackId);
    List<Long> getNotInCards(LearnCourse learnCourse, Long qPackId);
    void addCardsToCourse(Long courseId, List<Long> newCardsToAdd);
    void addCardsToCourse(LearnCourse learnCourse, List<Long> newCardsToAdd);
    Long addCourseForQPack(String courseTitle, Long qPackId);

    void saveCourse(LearnCourse learnCourse);

    LearnCourse getTempCourseFor(Long qPackId, List<Long> cardIds, boolean shuffleCards);
    LearnCourse getTempCourseFor(Long qPackId, boolean shuffleCards);
}
