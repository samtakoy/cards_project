package ru.samtakoy.core.services;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationsHelper {

    // TODO refactor to notification settings object?

    // каналы
    public static final String GROUP_ID = "group_01";
    public static final String CHANNEL_ID = "channel_01";


    private static boolean isChannelsInited = false;

    public static void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // less than v26
            return;
        }
        if(isChannelsInited) { return; } else {
            isChannelsInited = true;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(GROUP_ID, "Group name"));

        initOneChannel(notificationManager, CHANNEL_ID, "Channel name", "Channel desc", GROUP_ID);
    }

    private static void initOneChannel(NotificationManager notificationManager, String channelId, String channelName, String channelDesc, String groupId){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // less than v26
            return;
        }

        NotificationChannel channel = new NotificationChannel( channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT );
        channel.setDescription(channelDesc);
        channel.setGroup(groupId);
        notificationManager.createNotificationChannel(channel);
    }

}
