package ru.samtakoy.data.learncourse.model.schedule

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

        fun deserializeFrom(srcString: String?): ScheduleEntity {
            return deserializeFromString(srcString)
        }

        @Deprecated("Дублирование метода")
        private fun deserializeFromString(srcString: String?): ScheduleEntity {
            if (srcString == null || srcString.length == 0) {
                return ScheduleEntity(emptyList())
            }

            val parts: List<String> = srcString.split(ID_DELIMITTER)
            val newItems = mutableListOf<ScheduleItemEntity>()
            for (oneStringPart in parts) {
                val item = ScheduleItemEntity.parseString(oneStringPart)
                newItems.add(item)
            }
            return ScheduleEntity(newItems.toList())
        }
    }
}
