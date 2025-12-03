package ru.samtakoy.oldlegacy.features.notifications

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationsHelper {
    // TODO refactor to notification settings object?
    // каналы
    const val GROUP_ID: String = "group_01"
    const val CHANNEL_ID: String = "channel_01"

    private var isChannelsInited = false

    fun initChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // less than v26
            return
        }
        if (isChannelsInited) {
            return
        } else {
            isChannelsInited = true
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroup(NotificationChannelGroup(GROUP_ID, "Group name"))

        initOneChannel(notificationManager, CHANNEL_ID, "Channel name", "Channel desc", GROUP_ID)
    }

    private fun initOneChannel(
        notificationManager: NotificationManager,
        channelId: String,
        channelName: String,
        channelDesc: String,
        groupId: String
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // less than v26
            return
        }

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.setDescription(channelDesc)
        channel.setGroup(groupId)
        notificationManager.createNotificationChannel(channel)
    }
}
