package ru.samtakoy.core.data.local.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule

class ScheduleStringConverter {

    @TypeConverter
    fun fromSchedule(schedule: Schedule): String = schedule.serializeToString()

    @TypeConverter
    fun fromString(string: String): Schedule = Schedule.deserializeFrom(string)

}