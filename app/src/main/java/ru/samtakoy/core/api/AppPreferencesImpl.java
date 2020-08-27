package ru.samtakoy.core.api;

import android.content.Context;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import static ru.samtakoy.core.api.AppPreferencesKeysKt.NEW_LEARNS_NOTIFICATION_MIN_UTC;
import static ru.samtakoy.core.api.AppPreferencesKeysKt.NEW_REPEATS_NOTIFICATION_MIN_UTC;
import static ru.samtakoy.core.api.AppPreferencesKeysKt.UNCOMPLETED_NOTIFICATION_MIN_UTC;

public class AppPreferencesImpl implements AppPreferences {


    /*private static final String NEW_REPEATS_NOTIFICATION_MIN_UTC = "NEW_REPEATS_NOTIFICATION_MIN_UTC";
    private static final String NEW_LEARNS_NOTIFICATION_MIN_UTC = "NEW_LEARNS_NOTIFICATION_MIN_UTC";
    private static final String UNCOMPLETED_NOTIFICATION_MIN_UTC = "UNCOMPLETED_NOTIFICATION_MIN_UTC";*/


    @Inject
    Context mContext;

    @Inject
    public AppPreferencesImpl() {
        //mContext = ctx;
    }

    // --

    public long getNewRepeatsNotificationMinUtc() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getLong(NEW_REPEATS_NOTIFICATION_MIN_UTC, 0);
    }

    public void setNewRepeatsNotificationMinUtc(long value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(NEW_REPEATS_NOTIFICATION_MIN_UTC, value)
                .apply();
    }

    // --

    public String getString(String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(key, defValue);
    }

    public void setString(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(key, value)
                .apply();
    }

    // --

    public long getNewLearnsNotificationMinUtc() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getLong(NEW_LEARNS_NOTIFICATION_MIN_UTC, 0);
    }

    public void setNewLearnsNotificationMinUtc(long value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(NEW_LEARNS_NOTIFICATION_MIN_UTC, value)
                .apply();
    }

    // --


    // --

    public long getUncompletedNotificationMinUtc() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getLong(UNCOMPLETED_NOTIFICATION_MIN_UTC, 0);
    }

    public void setUncompletedNotificationMinUtc(long value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(UNCOMPLETED_NOTIFICATION_MIN_UTC, value)
                .apply();
    }

    // --

}
