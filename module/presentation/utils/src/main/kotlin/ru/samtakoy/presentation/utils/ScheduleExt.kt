package ru.samtakoy.presentation.utils

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.ScheduleItem
import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit

fun Schedule.toStringViewWithPrev(resources: Resources, prevSchedule: Schedule): String {
    return "(" + prevSchedule.lastItem + ")" + toStringView(resources)
}

fun Schedule.toStringView(resources: Resources): String {
    val sb = StringBuilder()
    val itr = items.iterator()
    while (itr.hasNext()) {
        val item = itr.next()
        if (sb.length != 0) {
            sb.append(" ")
        }
        sb.append(item.toStringView(resources))
    }
    return sb.toString()
}

fun ScheduleItem.toStringView(resources: Resources): String {
    return dimension.toString() + resources.getString(timeUnit.toStringId())
}
fun ScheduleTimeUnit.toStringId(): Int {
    return when (this) {
        ScheduleTimeUnit.MINUTE -> R.string.time_min
        ScheduleTimeUnit.HOUR -> R.string.time_hour
        ScheduleTimeUnit.DAY -> R.string.time_day
        ScheduleTimeUnit.WEEK -> R.string.time_week
        ScheduleTimeUnit.MONTH -> R.string.time_month
        ScheduleTimeUnit.YEAR -> R.string.time_year
    }
}