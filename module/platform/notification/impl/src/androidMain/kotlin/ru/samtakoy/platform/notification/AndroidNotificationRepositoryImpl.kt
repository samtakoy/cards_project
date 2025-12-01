package ru.samtakoy.platform.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.notification_import_zip_title

@SuppressLint("ObsoleteSdkInt")
internal class AndroidNotificationRepositoryImpl(
    private val context: Context
) : AndroidNotificationRepository {
    override fun getImportZipNotificationId(): Int = NotificationConst.IMPORT_ZIP_NOTIFICATION_ID

    override suspend fun buildImportZipNotification(
        zipName: String,
        status: String,
        curProgress: Int,
        maxProgress: Int
    ): Notification {
        val channelId = prepareWorkChannel(
            NotificationConst.WORK_CHANNEL_ID,
            NotificationConst.WORK_CHANNEL_NAME_STRING_ID
        )

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(
                getString(
                    Res.string.notification_import_zip_title,
                    zipName
                )
            )
            .setContentText(status)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(
                maxProgress,
                curProgress,
                false
            )
            .setOngoing(true)
            .build()
    }

    override fun getSpeechNotificationId(): Int = NotificationConst.SPEECH_NOTIFICATION_ID

    override suspend fun buildSpeechNotification(
        remoteViewsBig: RemoteViews?,
        remoteViewsShort: RemoteViews?,
        clickIntent: PendingIntent?
    ): Notification {
        val channelId = prepareWorkChannel(
            channelId = NotificationConst.SPEECH_CHANNEL_ID,
            channelNameStringRes = NotificationConst.SPEECH_CHANNEL_NAME_STRING_ID
        )

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.presence_audio_online)
            .setOngoing(true)
            .setSilent(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomBigContentView(remoteViewsBig)
            .setCustomContentView(remoteViewsShort)
            .also { builder ->
                clickIntent?.let { builder.setContentIntent(clickIntent) }
            }
            .setAutoCancel(false)
            .build()
    }

    private suspend fun prepareWorkChannel(
        channelId: String,
        channelNameStringRes: StringResource,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(channelNameStringRes),
                importance
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return channelId
    }
}