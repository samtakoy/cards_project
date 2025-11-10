package ru.samtakoy.data.common.db.converters

import androidx.room.TypeConverter
import ru.samtakoy.common.utils.DateUtils
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/*
@OptIn(ExperimentalTime::class)
internal class DateLongConverter {

    @TypeConverter
    fun fromDate(date: Instant): Long = DateUtils.dateToDbSerialized(date)

    @TypeConverter
    fun fromTimestamp(timestamp: Long): Instant = DateUtils.dateFromDbSerialized(timestamp)

}*/