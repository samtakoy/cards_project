package ru.samtakoy.core.services.learn_courses;

import android.app.PendingIntent;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import ru.samtakoy.R;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;

import static ru.samtakoy.core.services.NotificationsConst.NOTIFICATION_ID_NEW_LEARNINGS_AVAILABLE;
import static ru.samtakoy.core.services.NotificationsConst.REQ_CODE_NEW_LEARNINGS_ALARM;
import static ru.samtakoy.core.services.NotificationsConst.REQ_CODE_NEW_LEARNINGS_CANCEL;
import static ru.samtakoy.core.services.NotificationsConst.REQ_CODE_NEW_LEARNINGS_CLICK;

public class LearnsApi extends CoursesApi {

    @Inject
    public LearnsApi() {
    }

    @Override
    @NotNull
    protected LearnCourseMode getLearnCourseMode() {
        return LearnCourseMode.LEARN_WAITING;
    }

    public void shiftLearnCourses() {
        shiftLearnCourses(getLearnCourseMode(), getShiftMillis());
        rescheduleNewLearnCourses();
    }

    /** обновить нотификации и таймеры напоминания */
    public void rescheduleNewLearnCourses(){

        LearnCourseMode lcMode = getLearnCourseMode();
        int notificationId =       NOTIFICATION_ID_NEW_LEARNINGS_AVAILABLE;
        int notificationIconId =   android.R.drawable.ic_menu_report_image;
        String notificationTitle = getString(R.string.notifications_learn_waitings_title);
        String notificationText =  getString(R.string.notifications_learn_waitings_text);

        PendingIntent clickIntent = PendingIntent.getService(mContext, REQ_CODE_NEW_LEARNINGS_CLICK, getShowUiIntent(), 0);
        PendingIntent cancelIntent = PendingIntent.getService(mContext, REQ_CODE_NEW_LEARNINGS_CANCEL, getShiftIntent(), 0);

        rescheduleLearnCourses(lcMode, notificationId, notificationIconId, notificationTitle, notificationText, clickIntent, cancelIntent);
    }

    @Override
    protected PendingIntent getLearnCoursesAlarmPendingIntent(boolean noCreate){
        return PendingIntent.getService(mContext, REQ_CODE_NEW_LEARNINGS_ALARM, getReSchedulingIntent(), noCreate ? PendingIntent.FLAG_NO_CREATE : 0);
    }


}
