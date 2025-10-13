package ru.samtakoy.data.common.db.converters

import androidx.room.TypeConverter
import ru.samtakoy.common.utils.DateUtils
import java.util.Date

internal class DateLongConverter {

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun fromTimestamp(timestamp: Long): Date = DateUtils.getDateFromLong(timestamp)

}