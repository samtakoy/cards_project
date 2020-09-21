package ru.samtakoy.core.data.local.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.app.utils.DateUtils

class DateLongConverter {

    @TypeConverter
    fun fromDate(date: java.util.Date): Long = date.time

    @TypeConverter
    fun fromTimestamp(timestamp: Long): java.util.Date = DateUtils.getDateFromLong(timestamp)

}