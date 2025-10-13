package ru.samtakoy.domain.learncourse.schedule

data class Schedule(
    val items: List<ScheduleItem>
) {
    val isEmpty: Boolean
        get() = items.isEmpty()

    val firstItem: ScheduleItem?
        get() {
            if (this.isEmpty) {
                return null
            }
            return items.first()
        }

    val lastItem: ScheduleItem?
        get() {
            if (this.isEmpty) {
                return null
            }
            return items.last()
        }

    companion object {
        val DEFAULT: Schedule = Schedule(
            listOf(
                ScheduleItem(30, ScheduleTimeUnit.MINUTE),
                ScheduleItem(1, ScheduleTimeUnit.HOUR),
                ScheduleItem(2, ScheduleTimeUnit.HOUR),
                ScheduleItem(4, ScheduleTimeUnit.HOUR),
                ScheduleItem(8, ScheduleTimeUnit.HOUR),
                ScheduleItem(1, ScheduleTimeUnit.DAY),
                ScheduleItem(3, ScheduleTimeUnit.DAY),
                ScheduleItem(1, ScheduleTimeUnit.WEEK),
                ScheduleItem(2, ScheduleTimeUnit.WEEK),
                ScheduleItem(1, ScheduleTimeUnit.MONTH),
                ScheduleItem(2, ScheduleTimeUnit.MONTH),
                ScheduleItem(6, ScheduleTimeUnit.MONTH),
                ScheduleItem(1, ScheduleTimeUnit.YEAR)
            )
        )
    }
}