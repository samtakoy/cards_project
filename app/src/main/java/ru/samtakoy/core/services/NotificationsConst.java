package ru.samtakoy.core.services;

import ru.samtakoy.core.model.elements.ScheduleTimeUnit;

public class NotificationsConst {

    public static final int NEW_COURSE_LEARN_DEFAULT_MILLIS_DELTA = 3*ScheduleTimeUnit.MINUTE.getMillis();

    // магические константы
    public static final long DELTA = ScheduleTimeUnit.MINUTE.getMillis();
    //public static final long SMALL_DELTA = 30*1000;

    // коды PendingIntent
    public static final int REQ_CODE_NEW_REPEATINGS_CLICK = 1;
    public static final int REQ_CODE_NEW_REPEATINGS_CANCEL = 2;
    public static final int REQ_CODE_NEW_REPEATINGS_ALARM = 3;

    // коды PendingIntent
    public static final int REQ_CODE_NEW_LEARNINGS_CLICK = 4;
    public static final int REQ_CODE_NEW_LEARNINGS_CANCEL = 5;
    public static final int REQ_CODE_NEW_LEARNINGS_ALARM = 6;

    public static final int REQ_CODE_UNCOMPLETED_TASKS_CLICK = 7;
    public static final int REQ_CODE_UNCOMPLETED_TASKS_CANCEL = 8;
    public static final int REQ_CODE_UNCOMPLETED_TASKS_ALARM = 9;


    static final int NOTIFICATIONS_ID_START = 100;
    public static final int NOTIFICATION_ID_NEW_REPEATING_AVAILABLE = NOTIFICATIONS_ID_START+0;
    public static final int NOTIFICATION_ID_NEW_LEARNINGS_AVAILABLE = NOTIFICATIONS_ID_START+1;
    public static final int NOTIFICATION_ID_UNCOMPLETED_TASKS = NOTIFICATIONS_ID_START+2;

}
