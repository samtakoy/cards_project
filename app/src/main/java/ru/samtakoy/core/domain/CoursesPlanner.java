package ru.samtakoy.core.domain;

import androidx.annotation.Nullable;

import java.util.List;

import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule;

public interface CoursesPlanner {


    // проверить позднее - есть ли начатые незавершенные обучения/повторения и показать нотификацию
    void planUncompletedTasksChecking();

    void reScheduleLearnCourses();

    void planAdditionalCards(Long qPackId, List<Long> errorCardIds, Schedule schedule);

    void planAdditionalCards(Long qPackId, @Nullable String subTitle, List<Long> cardIds, Schedule restSchedule);
}
