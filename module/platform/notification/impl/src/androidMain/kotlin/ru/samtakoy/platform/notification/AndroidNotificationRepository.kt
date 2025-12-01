package ru.samtakoy.platform.notification

import android.app.Notification
import android.app.PendingIntent
import android.widget.RemoteViews

interface AndroidNotificationRepository {
    fun getImportZipNotificationId(): Int

    suspend fun buildImportZipNotification(
        zipName: String,
        status: String,
        curProgress: Int,
        maxProgress: Int
    ): Notification

    fun getSpeechNotificationId(): Int

    suspend fun buildSpeechNotification(
        remoteViewsBig: RemoteViews?,
        remoteViewsShort: RemoteViews?,
        clickIntent: PendingIntent?
    ): Notification
}