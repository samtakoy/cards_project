package ru.samtakoy.domain.learncourse

import ru.samtakoy.domain.learncourse.schedule.Schedule

interface CoursesPlanner {
    // проверить позднее - есть ли начатые незавершенные обучения/повторения и показать нотификацию
    fun planUncompletedTasksChecking()

    fun reScheduleLearnCourses()

    fun planAdditionalCards(qPackId: Long, errorCardIds: List<Long>, schedule: Schedule)

    // todo suspend
    fun planAdditionalCards(qPackId: Long, subTitle: String?, cardIds: List<Long>, restSchedule: Schedule)
}
