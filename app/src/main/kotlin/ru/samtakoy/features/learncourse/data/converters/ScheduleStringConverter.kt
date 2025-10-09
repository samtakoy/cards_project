package ru.samtakoy.features.learncourse.data.converters

import androidx.room.TypeConverter
import ru.samtakoy.features.learncourse.data.model.schedule.ScheduleEntity

class ScheduleStringConverter {
    @TypeConverter
    fun fromSchedule(schedule: ScheduleEntity): String {
        return schedule.serializeToString()
    }

    @TypeConverter
    fun fromString(string: String): ScheduleEntity {
        return ScheduleEntity.Companion.deserializeFrom(string)
    }
}