package ru.samtakoy.core.business;

import androidx.annotation.Nullable;

import java.util.List;

import ru.samtakoy.core.database.room.entities.elements.Schedule;

public interface CoursesPlanner {


    void reScheduleLearnCourses();

    void planAdditionalCards(Long qPackId, List<Long> errorCardIds, Schedule schedule);

    void planAdditionalCards(Long qPackId, @Nullable String subTitle, List<Long> cardIds, Schedule restSchedule);
}
