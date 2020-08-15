package ru.samtakoy.core.services.learn_courses;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.elements.ScheduleTimeUnit;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.services.AppPreferences;
import ru.samtakoy.core.services.NotificationsPlannerService;

import static ru.samtakoy.core.services.NotificationsConst.NOTIFICATION_ID_UNCOMPLETED_TASKS;
import static ru.samtakoy.core.services.NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_ALARM;
import static ru.samtakoy.core.services.NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_CANCEL;
import static ru.samtakoy.core.services.NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_CLICK;
import static ru.samtakoy.core.services.NotificationsHelper.CHANNEL_ID;

public class UncompletedTaskInteractor {

    //
    //.загрузка: актуализация и показ нотификаций
    //.
    //.начали курс:           запланировать отложенную проверку состояния
    //.возобновили курс:      запланировать отложенную проверку состояния
    //.нажали на нотификацию: запланировать отложенную проверку состояния
    //.смахнули нотификацию:  отложить нотификации
    //


    private static List<LearnCourseMode> sUncompletedCourseModes = null;

    protected Context mContext;
    protected AppPreferences mPreferences;
    private UncompletedTaskSettings mS = new UncompletedTaskSettings(
            30*ScheduleTimeUnit.MINUTE.getMillis(),
            30*ScheduleTimeUnit.MINUTE.getMillis()
    );

    public UncompletedTaskInteractor(Context context, AppPreferences preferences){
        mContext = context;
        //mS = settings;
        mPreferences = preferences;
    }

    public void dispose(){
        mContext = null;
        mPreferences = null;
    }

    protected Context getAppContext(){
        return  mContext.getApplicationContext();
    }

    protected String getString(@StringRes int id){
        return mContext.getResources().getString(id);
    }

    public void onUncompletedTaskShow(){
        cancelAllAlarmsAndNotifications();
        // запланировать проверку
        //shiftUncompletedChecking();
        planCheckingAlarm(mS.getOpenCourseShiftMillis());
    }

    public void checkAndNotifyAboutUncompletedTasks(boolean onBoot) {

        List<LearnCourse> uncompletedCourses = getUncompletedCourses();

        if(uncompletedCourses.size() == 0){
            // отменить все таймеры и нотификации
            cancelAllAlarmsAndNotifications();
            return;
        }

        if(!onBoot){

        }

        showNotification();
    }

    private void showNotification() {
        // NOTIFICATION_ID_UNCOMPLETED_TASKS;
        String notificationTitle = getString(R.string.notifications_uncompleted_title);
        String notificationText =  getString(R.string.notifications_uncompleted_text);

        PendingIntent clickIntent = PendingIntent.getService(getAppContext(), REQ_CODE_UNCOMPLETED_TASKS_CLICK, NotificationsPlannerService.getShowUncompletedTasksIntent(getAppContext()), 0);
        PendingIntent shiftIntent = PendingIntent.getService(getAppContext(), REQ_CODE_UNCOMPLETED_TASKS_CANCEL, NotificationsPlannerService.getSchiftUncompletedCheckingIntent(getAppContext()), 0);

        // показать нотификацию
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setTicker(notificationTitle)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(clickIntent)
                .setDeleteIntent(shiftIntent)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat.from(mContext).notify(NOTIFICATION_ID_UNCOMPLETED_TASKS, notification);
    }

    @NotNull
    public List<LearnCourseMode> getUncompletedCourseModes() {
        if(sUncompletedCourseModes == null) {
            sUncompletedCourseModes = new ArrayList<LearnCourseMode>(
                    EnumSet.of(LearnCourseMode.LEARNING, LearnCourseMode.REPEATING)
            );
        }
        return sUncompletedCourseModes;
    }

    @NotNull
    public List<LearnCourse> getUncompletedCourses() {
        return ContentProviderHelper.getUncompletedCourses(mContext);
    }

    private void cancelAllAlarmsAndNotifications() {
        cancelNotification();
        cancelAlarmByPendingIntent(getAlarmPendingIntent(true));
    }

    private void cancelNotification() {
        NotificationManagerCompat.from(mContext).cancel(NOTIFICATION_ID_UNCOMPLETED_TASKS);
    }

    /** сдвинуть проверку неоконченных */
    public void shiftUncompletedChecking() {
        cancelAllAlarmsAndNotifications();

        int shiftMillis = mS.getShiftMillis();

        mPreferences.setUncompletedNotificationMinUtc(DateUtils.getDateAfterCurrentLong(shiftMillis));

        planCheckingAlarm(shiftMillis);
    }

    private void planCheckingAlarm(int shiftMillis) {

        PendingIntent pIntent = getAlarmPendingIntent(false);
        AlarmManager aManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        long targetTime = SystemClock.elapsedRealtime() + shiftMillis;
        //AlarmManagerCompat.setExact(aManager, AlarmManager.ELAPSED_REALTIME,  targetTime, pIntent);
        aManager.set(AlarmManager.ELAPSED_REALTIME,  targetTime, pIntent);
    }

    private PendingIntent getAlarmPendingIntent(boolean noCreate) {
        return PendingIntent.getService(getAppContext(), REQ_CODE_UNCOMPLETED_TASKS_ALARM, NotificationsPlannerService.getCheckUncompletedTasksIntent(getAppContext()), noCreate ?  PendingIntent.FLAG_NO_CREATE: 0);
    }

    /** отменить AlarmManager по PendingIntent */
    protected void cancelAlarmByPendingIntent(PendingIntent pIntent) {
        if(pIntent != null) {
            AlarmManager aManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            aManager.cancel(pIntent);
            pIntent.cancel();
        }
    }
}
