package ru.samtakoy.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    val LOCALE_RU = Locale("ru", "RU")
    const val DATE_PATTERN = "dd-MM-yyyy HH:mm:ss"
    val DATE_FORMAT = SimpleDateFormat(DATE_PATTERN, LOCALE_RU)

    fun getTimeLong(date: Date): Long {
        return date.getTime()
    }

    val currentTimeLong: Long
        get() = System.currentTimeMillis()

    fun getMillisTo(targetUtc: Long): Long {
        return targetUtc - System.currentTimeMillis()
    }

    fun getMillisTo(date: Date): Long {
        return getMillisTo(dateToDbSerialized(date))
    }

    val currentTimeDate: Date
        get() = getDateFromLong(currentTimeLong)

    fun getDateAfterCurrentLong(millisDelta: Int): Long {
        return currentTimeLong + millisDelta
    }

    fun dateToDbSerialized(d: Date): Long {
        return getTimeLong(d)
    }

    fun dateFromDbSerialized(utcTimeMillis: Long): Date {
        return getDateFromLong(utcTimeMillis)
    }

    fun getDateFromLong(value: Long): Date {
        return Date(value)
    }
}
