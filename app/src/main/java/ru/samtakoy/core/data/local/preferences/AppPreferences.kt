package ru.samtakoy.core.data.local.preferences

interface AppPreferences {


    fun setUncompletedNotificationMinUtc(value: Long);

    fun getString(key: String, defValue: String?): String?
    fun setString(key: String, value: String?)
}