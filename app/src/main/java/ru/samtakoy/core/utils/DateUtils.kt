package ru.samtakoy.core.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    val LOCALE_RU = Locale("ru", "RU")
    const val DATE_PATTERN = "dd-MM-yyyy HH:mm:ss"
    val DATE_FORMAT = SimpleDateFormat(DATE_PATTERN, LOCALE_RU)
}