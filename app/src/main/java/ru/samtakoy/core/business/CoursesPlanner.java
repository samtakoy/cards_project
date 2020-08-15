package ru.samtakoy.core.business;

import java.util.List;

import ru.samtakoy.core.model.elements.Schedule;

public interface CoursesPlanner {


    void reScheduleLearnCourses();
    void planAdditionalCards(Long qPackId, List<Long> errorCardIds, Schedule schedule);

}
