package ru.samtakoy.features.learncourse.domain.model.schedule

import ru.samtakoy.core.app.some.Resources

data class ScheduleItem(
    val dimension: Int,
    val timeUnit: ScheduleTimeUnit
) : Comparable<Any?> {
    override fun compareTo(o: Any?): Int {
        val other = o as ScheduleItem
        if (other.timeUnit == this.timeUnit) {
            return this.dimension - other.dimension
        }
        return timeUnit.ordinal - other.timeUnit.ordinal
    }

    fun toStringView(resources: Resources): String {
        return dimension.toString() + resources.getString(timeUnit.textStringId)
    }

    val millis: Int
        get() = this.dimension * timeUnit.getMillis()
}