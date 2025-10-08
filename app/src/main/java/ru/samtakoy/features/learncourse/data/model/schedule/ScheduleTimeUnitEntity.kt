package ru.samtakoy.features.learncourse.data.model.schedule

enum class ScheduleTimeUnitEntity(id: String) {
    MINUTE("min"),
    HOUR("h"),
    DAY("d"),
    WEEK("w"),
    MONTH("mon"),
    YEAR("y");

    val id: String

    init {
        this.id = id
    }

    companion object {
        private val sMapById = mutableMapOf<String, ScheduleTimeUnitEntity>()

        init {
            for (oneItem in entries) {
                sMapById.put(oneItem.id, oneItem)
            }
        }

        fun valueOfId(s: String): ScheduleTimeUnitEntity? {
            return sMapById.get(s)
        }
    }
}
