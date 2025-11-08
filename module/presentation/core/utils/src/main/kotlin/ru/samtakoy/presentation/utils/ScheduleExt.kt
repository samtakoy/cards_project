package ru.samtakoy.presentation.utils

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.ScheduleItem
import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.time_day
import ru.samtakoy.resources.time_hour
import ru.samtakoy.resources.time_min
import ru.samtakoy.resources.time_month
import ru.samtakoy.resources.time_week
import ru.samtakoy.resources.time_year

suspend fun Schedule.toStringViewWithPrev(prevSchedule: Schedule): String {
    return "(" + prevSchedule.lastItem + ")" + toStringView()
}

suspend fun Schedule.toStringView(): String {
    val sb = StringBuilder()
    val itr = items.iterator()
    while (itr.hasNext()) {
        val item = itr.next()
        if (sb.length != 0) {
            sb.append(" ")
        }
        sb.append(item.toStringView())
    }
    return sb.toString()
}

suspend fun ScheduleItem.toStringView(): String {
    return dimension.toString() + getString(timeUnit.toStringId())
}

fun ScheduleTimeUnit.toStringId(): StringResource {
    return when (this) {
        ScheduleTimeUnit.MINUTE -> Res.string.time_min
        ScheduleTimeUnit.HOUR -> Res.string.time_hour
        ScheduleTimeUnit.DAY -> Res.string.time_day
        ScheduleTimeUnit.WEEK -> Res.string.time_week
        ScheduleTimeUnit.MONTH -> Res.string.time_month
        ScheduleTimeUnit.YEAR -> Res.string.time_year
    }
}

@Deprecated("use suspend function")
fun ScheduleTimeUnit.toStringView(): String {
    return runBlocking { getString(toStringId()) }
}