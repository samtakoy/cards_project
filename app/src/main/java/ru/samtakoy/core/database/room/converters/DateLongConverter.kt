package ru.samtakoy.core.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.utils.DateUtils

class DateLongConverter {

    @TypeConverter
    fun fromDate(date: java.util.Date): Long = date.time

    @TypeConverter
    fun fromTimestamp(timestamp: Long): java.util.Date = DateUtils.getDateFromLong(timestamp)

}