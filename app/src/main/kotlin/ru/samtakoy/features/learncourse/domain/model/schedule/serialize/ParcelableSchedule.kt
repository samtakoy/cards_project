package ru.samtakoy.features.learncourse.domain.model.schedule.serialize

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ParcelableSchedule(
    val items: List<Item>
) : Parcelable {

    @Parcelize
    class Item(
        val dimension: Int,
        val timeUnit: TimeUnit
    ) : Parcelable

    enum class TimeUnit {
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }
}