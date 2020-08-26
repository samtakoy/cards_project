package ru.samtakoy.core.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.database.room.entities.elements.Schedule

class ScheduleStringConverter {

    @TypeConverter
    fun fromSchedule(schedule: Schedule) = schedule.toString()

    @TypeConverter
    fun fromString(string: String) = Schedule.fromString(string)

}