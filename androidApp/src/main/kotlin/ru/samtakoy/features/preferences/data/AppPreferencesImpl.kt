package ru.samtakoy.features.preferences.data

import android.content.Context
import android.preference.PreferenceManager

/** TODO переделать (префы с привязкой к имени файла всесто app) */
class AppPreferencesImpl(
    private val context: Context
) : AppPreferences {

    var newRepeatsNotificationMinUtc: Long
        // --
        get() = PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(NEW_REPEATS_NOTIFICATION_MIN_UTC, 0)
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(NEW_REPEATS_NOTIFICATION_MIN_UTC, value)
                .apply()
        }

    // --
    override fun getString(key: String, defValue: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(key, defValue)
    }

    override fun setString(key: String, value: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(key, value)
            .apply()
    }

    var newLearnsNotificationMinUtc: Long
        // --
        get() = PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(NEW_LEARNS_NOTIFICATION_MIN_UTC, 0)
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(NEW_LEARNS_NOTIFICATION_MIN_UTC, value)
                .apply()
        }

    // --
    // --
    fun getUncompletedNotificationMinUtc(): Long {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getLong(UNCOMPLETED_NOTIFICATION_MIN_UTC, 0)
    }

    override fun setUncompletedNotificationMinUtc(value: Long) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putLong(UNCOMPLETED_NOTIFICATION_MIN_UTC, value)
            .apply()
    }
}
