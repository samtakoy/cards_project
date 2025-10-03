package ru.samtakoy.core.data.local.database.room.entities.elements

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

    @Deprecated("")
    override fun toString(): String {
        return dimension.toString() + timeUnit.getId()
    }

    fun serializeToString(): String {
        return dimension.toString() + timeUnit.getId()
    }

    fun toStringView(resources: Resources): String {
        return dimension.toString() + resources.getString(timeUnit.getStringId())
    }

    val millis: Int
        get() = this.dimension * timeUnit.getMillis()

    companion object {
        @JvmStatic
        fun parseString(srcString: String): ScheduleItem {
            val part: Array<String?> =
                srcString.split("(?<=\\d)(?=\\D)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val digit = part[0]!!.toInt()
            val timeUnit = ScheduleTimeUnit.valueOfId(part[1])
            return ScheduleItem(digit, timeUnit)
        }
    }
}