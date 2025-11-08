package ru.samtakoy.platform.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import org.jetbrains.compose.resources.getString
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.notification_import_zip_title

class AndroidNotificationRepositoryImpl(
    private val context: Context
) {
    fun getImportZipNotificationId(): Int = NotificationConst.IMPORT_ZIP_NOTIFICATION_ID

    suspend fun buildImportZipNotification(
        zipName: String,
        status: String,
        curProgress: Int,
        maxProgress: Int
    ): Notification {
        val channelId = NotificationConst.WORK_CHANNEL_ID

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(NotificationConst.WORK_CHANNEL_NAME_STRING_ID),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(
                getString(
                    Res.string.notification_import_zip_title,
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