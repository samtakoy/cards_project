package ru.samtakoy.core.data.local.database.room.entities.elements

import kotlinx.collections.immutable.toImmutableList
import okhttp3.internal.immutableListOf
import org.apache.commons.lang3.StringUtils
import ru.samtakoy.core.app.some.Resources
import java.util.TreeSet

data class Schedule(
    val mItems: List<ScheduleItem> = immutableListOf<ScheduleItem>()
) {
    fun serializeToString(): String {
        return mItems.joinToString(
            ","
        ) {
            it.serializeToString()
        }
    }

    fun toStringViewWithPrev(resources: Resources, prevSchedule: Schedule): String {
        return "(" + prevSchedule.lastItem + ")" + toStringView(resources)
    }

    fun toStringView(resources: Resources): String {
        val sb = StringBuilder()
        val itr = mItems.iterator()
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
        get() = mItems.isEmpty()

    val firstItem: ScheduleItem?
        get() {
            if (this.isEmpty) {
                return null
            }
            return mItems.first()
        }

    val lastItem: ScheduleItem?
        get() {
            if (this.isEmpty) {
                return null
            }
            return mItems.last()
        }

    companion object {
        private const val ID_DELIMITTER = ","

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

        fun createEmpty(): Schedule {
            return Schedule()
        }

        @JvmStatic
        fun deserializeFrom(srcString: String?): Schedule {
            return deserializeFromString(srcString)
        }

        @Deprecated("Дублирование метода")
        private fun deserializeFromString(srcString: String?): Schedule {
            val newItems = TreeSet<ScheduleItem>()

            if (srcString == null || srcString.length == 0) {
                return Schedule(newItems.toImmutableList())
            }

            val parts: Array<String> = StringUtils.split(srcString, ID_DELIMITTER)
            for (oneStringPart in parts) {
                val item = ScheduleItem.parseString(oneStringPart)
                if (item == null) {
                    throw RuntimeException("unknown shedule item while string parsing")
                }
                newItems.add(item)
            }
            return Schedule(newItems.toImmutableList())
        }
    }
}
