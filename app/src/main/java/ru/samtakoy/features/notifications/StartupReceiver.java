package ru.samtakoy.features.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * TODO перейти на WorkManager?
 */
public class StartupReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(
                NotificationsPlannerService.getOnBootReSchedulingIntent(context)
        );
    }
}
