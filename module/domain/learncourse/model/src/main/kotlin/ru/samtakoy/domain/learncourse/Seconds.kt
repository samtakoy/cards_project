package ru.samtakoy.domain.learncourse

object Seconds {
    @JvmField
    val MINUTE: Int = 60
    @JvmField
    val HOUR: Int = 60 * MINUTE
    @JvmField
    val DAY: Int = 24 * HOUR
    @JvmField
    val WEEK: Int = 7 * DAY
    @JvmField
    val MONTH: Int = 30 * DAY
    @JvmField
    val YEAR: Int = 366 * DAY
}
