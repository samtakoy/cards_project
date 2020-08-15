package ru.samtakoy.core.services;

import android.content.Context;
import android.preference.PreferenceManager;

public class AppPreferences {

    private static final String NEW_REPEATS_NOTIFICATION_MIN_UTC = "NEW_REPEATS_NOTIFICATION_MIN_UTC";
    private static final String NEW_LEARNS_NOTIFICATION_MIN_UTC = "NEW_LEARNS_NOTIFICATION_MIN_UTC";
    private static final String UNCOMPLETED_NOTIFICATION_MIN_UTC = "UNCOMPLETED_NOTIFICATION_MIN_UTC";

    private Context mContext;

    public AppPreferences(Context ctx){
        mContext = ctx;
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
