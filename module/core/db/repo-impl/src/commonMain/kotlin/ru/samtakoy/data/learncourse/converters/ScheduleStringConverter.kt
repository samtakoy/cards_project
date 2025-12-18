package ru.samtakoy.data.learncourse.converters

import androidx.room.TypeConverter
import ru.samtakoy.data.learncourse.model.schedule.ScheduleEntity

internal class ScheduleStringConverter {
    @TypeConverter
    fun fromSchedule(schedule: ScheduleEntity): String {
        return schedule.serializeToString()
    }

    @TypeConverter
    fun fromString(string: String): ScheduleEntity {
        return ScheduleEntity.Companion.deserializeFrom(string)
    }
}