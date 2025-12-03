package ru.samtakoy.oldlegacy.features.notifications

import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit

object NotificationsConst {
    val NEW_COURSE_LEARN_DEFAULT_MILLIS_DELTA: Int = 3 * ScheduleTimeUnit.MINUTE.getMillis()

    // магические константы
    val DELTA: Long = ScheduleTimeUnit.MINUTE.getMillis().toLong()

    //public static final long SMALL_DELTA = 30*1000;
    // коды PendingIntent
    const val REQ_CODE_NEW_REPEATINGS_CLICK: Int = 1
    const val REQ_CODE_NEW_REPEATINGS_CANCEL: Int = 2
    const val REQ_CODE_NEW_REPEATINGS_ALARM: Int = 3

    // коды PendingIntent
    const val REQ_CODE_NEW_LEARNINGS_CLICK: Int = 4
    const val REQ_CODE_NEW_LEARNINGS_CANCEL: Int = 5
    const val REQ_CODE_NEW_LEARNINGS_ALARM: Int = 6

    const val REQ_CODE_UNCOMPLETED_TASKS_CLICK: Int = 7
    const val REQ_CODE_UNCOMPLETED_TASKS_CANCEL: Int = 8
    const val REQ_CODE_UNCOMPLETED_TASKS_ALARM: Int = 9

    const val NOTIFICATIONS_ID_START: Int = 100
    val NOTIFICATION_ID_NEW_REPEATING_AVAILABLE: Int = NOTIFICATIONS_ID_START + 0
    val NOTIFICATION_ID_NEW_LEARNINGS_AVAILABLE: Int = NOTIFICATIONS_ID_START + 1
    val NOTIFICATION_ID_UNCOMPLETED_TASKS: Int = NOTIFICATIONS_ID_START + 2
}
