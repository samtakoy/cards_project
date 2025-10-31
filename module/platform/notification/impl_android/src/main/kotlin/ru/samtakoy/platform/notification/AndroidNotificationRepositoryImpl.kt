package ru.samtakoy.platform.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.platform.notification.impl_android.R

class AndroidNotificationRepositoryImpl(
    private val context: Context,
    private val resources: Resources
) {
    fun getImportZipNotificationId(): Int = NotificationConst.IMPORT_ZIP_NOTIFICATION_ID

    fun buildImportZipNotification(
        zipName: String,
        status: String,
        curProgress: Int,
        maxProgress: Int
    ): Notification {
        val channelId = NotificationConst.WORK_CHANNEL_ID

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                resources.getString(NotificationConst.WORK_CHANNEL_NAME_STRING_ID),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(
                resources.getString(
                    R.string.notification_import_zip_title,
                    zipName
                )
            )
            .setTicker(status)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(
                maxProgress,
                curProgress,
                false
            )
            .setOngoing(true)
            .build()
    }
}