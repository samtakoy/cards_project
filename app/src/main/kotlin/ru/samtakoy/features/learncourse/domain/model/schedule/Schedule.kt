package ru.samtakoy.features.learncourse.domain.model.schedule

import ru.samtakoy.core.app.some.Resources

data class Schedule(
    val items: List<ScheduleItem>
) {
    fun toStringViewWithPrev(resources: Resources, prevSchedule: Schedule): String {
        return "(" + prevSchedule.lastItem + ")" + toStringView(resources)
    }

    fun toStringView(resources: Resources): String {
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