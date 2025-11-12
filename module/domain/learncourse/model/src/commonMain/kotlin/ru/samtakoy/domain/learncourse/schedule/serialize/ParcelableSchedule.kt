package ru.samtakoy.domain.learncourse.schedule.serialize

import kotlinx.serialization.Serializable

@Serializable
class ParcelableSchedule(
    val items: List<Item>
) {

    @Serializable
    class Item(
        val dimension: Int,
        val timeUnit: TimeUnit
    )

    enum class TimeUnit {
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }
}