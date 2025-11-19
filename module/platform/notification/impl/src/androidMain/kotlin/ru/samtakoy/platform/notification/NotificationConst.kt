package ru.samtakoy.platform.notification

import ru.samtakoy.resources.Res
import ru.samtakoy.resources.notification_speech_channel_name
import ru.samtakoy.resources.notification_work_channel_name

object NotificationConst {
    const val WORK_CHANNEL_ID = "work_channel_id"
    val WORK_CHANNEL_NAME_STRING_ID = Res.string.notification_work_channel_name
    const val IMPORT_ZIP_NOTIFICATION_ID = 1

    const val SPEECH_CHANNEL_ID = "speech_channel_id"
    val SPEECH_CHANNEL_NAME_STRING_ID = Res.string.notification_speech_channel_name
    const val SPEECH_NOTIFICATION_ID = 2
}