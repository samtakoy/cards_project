package ru.samtakoy.features.learncourse.domain

import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule

interface CoursesPlanner {
    // проверить позднее - есть ли начатые незавершенные обучения/повторения и показать нотификацию
    fun planUncompletedTasksChecking()

    fun reScheduleLearnCourses()

    fun planAdditionalCards(qPackId: Long, errorCardIds: List<Long>, schedule: Schedule)

    fun planAdditionalCards(qPackId: Long, subTitle: String?, cardIds: List<Long>, restSchedule: Schedule)
}
