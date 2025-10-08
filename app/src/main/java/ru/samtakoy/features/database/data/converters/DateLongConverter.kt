package ru.samtakoy.features.database.data.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.app.utils.DateUtils
import java.util.Date

class DateLongConverter {

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun fromTimestamp(timestamp: Long): Date = DateUtils.getDateFromLong(timestamp)

}