package ru.samtakoy.domain.learncourse.schedule

import ru.samtakoy.domain.learncourse.Seconds

enum class ScheduleTimeUnit(
    val mSeconds: Int
) {
    MINUTE(Seconds.MINUTE),
    HOUR(Seconds.HOUR),
    DAY(Seconds.DAY),
    WEEK(Seconds.WEEK),
    MONTH(Seconds.MONTH),
    YEAR(Seconds.YEAR);

    fun getMillis(): Int {
        return mSeconds * 1000
    }
}