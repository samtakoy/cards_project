package ru.samtakoy.oldlegacy.features.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * TODO перейти на WorkManager?
 */
class StartupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        context.startService(
            NotificationsPlannerService.getOnBootReSchedulingIntent(context)
        )
    }
}
