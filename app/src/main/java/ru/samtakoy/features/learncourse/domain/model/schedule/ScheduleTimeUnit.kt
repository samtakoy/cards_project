package ru.samtakoy.features.learncourse.domain.model.schedule

import androidx.annotation.StringRes
import ru.samtakoy.R
import ru.samtakoy.features.learncourse.domain.model.Seconds

enum class ScheduleTimeUnit(
    val mSeconds: Int,
    @StringRes
    val textStringId: Int
) {
    MINUTE(Seconds.MINUTE, R.string.time_min),
    HOUR(Seconds.HOUR, R.string.time_hour),
    DAY(Seconds.DAY, R.string.time_day),
    WEEK(Seconds.WEEK, R.string.time_week),
    MONTH(Seconds.MONTH, R.string.time_month),
    YEAR(Seconds.YEAR, R.string.time_year);

    fun getMillis(): Int {
        return mSeconds * 1000
    }
}