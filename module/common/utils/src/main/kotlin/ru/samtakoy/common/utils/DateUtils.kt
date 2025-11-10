package ru.samtakoy.common.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object DateUtils {

    /** Паттерн для форматирования "dd-MM-yyyy HH:mm:ss" */
    private val dateTimeFormat = LocalDateTime.Format {
        day(padding = Padding.ZERO)
        char('-')
        monthNumber()
        char('-')
        year()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
    }

    /** ISO 8601 формат с таймзоной: "2025-11-10T09:07:30+03:00" */
    private val iso8601WithZoneFormat = DateTimeComponents.Format {
        date(LocalDate.Formats.ISO)
        char('T')
        time(LocalTime.Formats.ISO)
        offset(UtcOffset.Formats.ISO)
    }

    /** Получить миллисекунды из Instant */
    @OptIn(ExperimentalTime::class)
    fun getTimeLong(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    /** Текущее время в миллисекундах */
    val currentTimeLong: Long
        @OptIn(ExperimentalTime::class)
        get() = Clock.System.now().toEpochMilliseconds()

    /** Миллисекунды до целевого времени */
    fun getMillisTo(targetUtc: Long): Long {
        @OptIn(ExperimentalTime::class)
        return targetUtc - Clock.System.now().toEpochMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    fun getMillisTo(instant: Instant): Long {
        return getMillisTo(dateToDbSerialized(instant))
    }

    /** Текущее время как Instant */
    @OptIn(ExperimentalTime::class)
    val currentTimeDate: Instant
        get() = Clock.System.now()

    /** Дата после текущего момента с добавлением миллисекунд */
    fun getDateAfterCurrentLong(millisDelta: Long): Long {
        return currentTimeLong + millisDelta
    }

    /** Сериализация Instant в Long (миллисекунды) */
    @OptIn(ExperimentalTime::class)
    fun dateToDbSerialized(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    /** Десериализация Long в Instant */
    @OptIn(ExperimentalTime::class)
    fun dateFromDbSerialized(utcTimeMillis: Long): Instant {
        return Instant.fromEpochMilliseconds(utcTimeMillis)
    }

    /** Форматирование Instant в строку с сохранением таймзоны */
    @OptIn(ExperimentalTime::class)
    fun formatWithZone(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        val localDateTime = instant.toLocalDateTime(timeZone)
        val offset = timeZone.offsetAt(instant)

        return buildString {
            append(localDateTime.format(LocalDateTime.Formats.ISO))
            append(offset.format(UtcOffset.Formats.ISO))
        }
    }

    /** Парсинг строки с таймзоной обратно в Instant */
    @OptIn(ExperimentalTime::class)
    fun parseWithZone(dateString: String): Instant {
        val components = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET.parse(dateString)
        return components.toInstantUsingOffset()
    }

    /** Форматирование Instant в строку (по паттерну dd-MM-yyyy HH:mm:ss) */
    @OptIn(ExperimentalTime::class)
    fun formatToString(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        val localDateTime = instant.toLocalDateTime(timeZone)
        return localDateTime.format(dateTimeFormat)
    }

    /** Парсинг строки в Instant (по паттерну dd-MM-yyyy HH:mm:ss) */
    @OptIn(ExperimentalTime::class)
    fun parseToDate(dateString: String, timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant {
        val localDateTime = LocalDateTime.parse(dateString, dateTimeFormat)
        return localDateTime.toInstant(timeZone)
    }
}