package ru.samtakoy.features.learncourse.data.model.schedule

import kotlinx.collections.immutable.toImmutableList
import okhttp3.internal.immutableListOf
import org.apache.commons.lang3.StringUtils
import ru.samtakoy.core.app.some.Resources
import java.util.TreeSet

class ScheduleEntity(
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
                return ScheduleEntity(newItems.toImmutableList())
            }

            val parts: Array<String> = StringUtils.split(srcString, ID_DELIMITTER)
            for (oneStringPart in parts) {
                val item = ScheduleItemEntity.parseString(oneStringPart)
                if (item == null) {
                    throw RuntimeException("unknown shedule item while string parsing")
                }
                newItems.add(item)
            }
            return ScheduleEntity(newItems.toImmutableList())
        }
    }
}
