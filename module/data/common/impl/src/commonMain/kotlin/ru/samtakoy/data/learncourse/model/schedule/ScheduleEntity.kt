package ru.samtakoy.data.learncourse.model.schedule

import java.util.TreeSet

internal class ScheduleEntity(
    val items: List<ScheduleItemEntity>
) {
    fun serializeToString(): String {
        return items.joinToString(
            ","
        ) {
            it.serializeToString()
        }
    }

    companion object {
        private const val ID_DELIMITTER = ","

        fun createEmpty(): ScheduleEntity {
            return ScheduleEntity(emptyList())
        }

        @JvmStatic
        fun deserializeFrom(srcString: String?): ScheduleEntity {
            return deserializeFromString(srcString)
        }

        @Deprecated("Дублирование метода")
        private fun deserializeFromString(srcString: String?): ScheduleEntity {
            val newItems = TreeSet<ScheduleItemEntity>()

            if (srcString == null || srcString.length == 0) {
                return ScheduleEntity(newItems.toList())
            }

            val parts: List<String> = srcString.split(ID_DELIMITTER)
            for (oneStringPart in parts) {
                val item = ScheduleItemEntity.parseString(oneStringPart)
                if (item == null) {
                    throw RuntimeException("unknown shedule item while string parsing")
                }
                newItems.add(item)
            }
            return ScheduleEntity(newItems.toList())
        }
    }
}
