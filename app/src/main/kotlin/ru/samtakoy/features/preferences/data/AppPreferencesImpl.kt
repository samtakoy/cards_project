package ru.samtakoy.features.preferences.data

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor() : AppPreferences {
    @JvmField
    @Inject
    var mContext: Context? = null

    var newRepeatsNotificationMinUtc: Long
        // --
        get() = PreferenceManager.getDefaultSharedPreferences(mContext)
            .getLong(NEW_REPEATS_NOTIFICATION_MIN_UTC, 0)
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(NEW_REPEATS_NOTIFICATION_MIN_UTC, value)
                .apply()
        }

    // --
    override fun getString(key: String, defValue: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
            .getString(key, defValue)
    }

    override fun setString(key: String, value: String?) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
            .edit()
            .putString(key, value)
            .apply()
    }

    var newLearnsNotificationMinUtc: Long
        // --
        get() = PreferenceManager.getDefaultSharedPreferences(mContext)
            .getLong(NEW_LEARNS_NOTIFICATION_MIN_UTC, 0)
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(NEW_LEARNS_NOTIFICATION_MIN_UTC, value)
                .apply()
        }

    // --
    // --
    fun getUncompletedNotificationMinUtc(): Long {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
            .getLong(UNCOMPLETED_NOTIFICATION_MIN_UTC, 0)
    }

    override fun setUncompletedNotificationMinUtc(value: Long) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
            .edit()
            .putLong(UNCOMPLETED_NOTIFICATION_MIN_UTC, value)
            .apply()
    }
}
