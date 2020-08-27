package ru.samtakoy.core.api

interface AppPreferences {


    fun setUncompletedNotificationMinUtc(value: Long);

    fun getString(key: String, defValue: String?): String?
    fun setString(key: String, value: String?)
}