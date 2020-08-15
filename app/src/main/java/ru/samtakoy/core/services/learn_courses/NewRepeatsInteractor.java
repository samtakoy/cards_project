package ru.samtakoy.core.services.learn_courses;

import android.app.PendingIntent;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourseMode;


import static ru.samtakoy.core.services.NotificationsConst.*;


public class NewRepeatsInteractor extends CoursesInteractor{

    public NewRepeatsInteractor(Context context){
        super(context);
    }

    @Override
    @NotNull
    protected LearnCourseMode getLearnCourseMode() {
        return LearnCourseMode.REPEAT_WAITING;
    }

    public void shiftNewRepeatings() {
        shiftLearnCourses(getLearnCourseMode(), getShiftMillis());
        rescheduleNewRepeatings();
    }

    /** обновить нотификации и таймеры напоминания */
    public void rescheduleNewRepeatings(){

        LearnCourseMode lcMode =   getLearnCourseMode();
        int notificationId =       NOTIFICATION_ID_NEW_REPEATING_AVAILABLE;
        int notificationIconId =   android.R.drawable.ic_menu_report_image;
        String notificationTitle = getString(R.string.notifications_new_repeats_title);
        String notificationText =  getString(R.string.notifications_new_repeats_text);
        PendingIntent clickIntent = PendingIntent.getService(mContext, REQ_CODE_NEW_REPEATINGS_CLICK,  getShowUiIntent(), 0);
        PendingIntent cancelIntent = PendingIntent.getService(mContext, REQ_CODE_NEW_REPEATINGS_CANCEL, getShiftIntent(), 0);

        rescheduleLearnCourses(lcMode, notificationId, notificationIconId, notificationTitle, notificationText, clickIntent, cancelIntent);
    }

    @Override
    protected PendingIntent getLearnCoursesAlarmPendingIntent(boolean noCreate){
        return PendingIntent.getService(mContext, REQ_CODE_NEW_REPEATINGS_ALARM, getReSchedulingIntent(), noCreate ? PendingIntent.FLAG_NO_CREATE : 0);
    }




}
