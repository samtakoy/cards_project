package ru.samtakoy.data.learncourse.model.schedule

internal class ScheduleItemEntity(
    val dimension: Int,
    val timeUnit: ScheduleTimeUnitEntity
) {
    @Deprecated("")
    override fun toString(): String {
        return dimension.toString() + timeUnit.id
    }

    fun serializeToString(): String {
        return dimension.toString() + timeUnit.id
    }

    companion object {
        @JvmStatic
        fun parseString(srcString: String): ScheduleItemEntity {
            val part: Array<String> =
                srcString.split("(?<=\\d)(?=\\D)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val digit = part[0].toInt()
            val timeUnit = ScheduleTimeUnitEntity.valueOfId(part[1])!!
            return ScheduleItemEntity(digit, timeUnit)
        }
    }
}