package ru.samtakoy.features.notifications.learn_courses;

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

import javax.inject.Inject;

import ru.samtakoy.R;
import ru.samtakoy.core.app.utils.DateUtils;
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.data.local.database.room.entities.elements.ScheduleTimeUnit;
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.data.local.preferences.AppPreferences;
import ru.samtakoy.core.domain.CoursesRepository;
import ru.samtakoy.features.notifications.NotificationsPlannerService;

import static ru.samtakoy.features.notifications.NotificationsConst.NOTIFICATION_ID_UNCOMPLETED_TASKS;
import static ru.samtakoy.features.notifications.NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_ALARM;
import static ru.samtakoy.features.notifications.NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_CANCEL;
import static ru.samtakoy.features.notifications.NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_CLICK;
import static ru.samtakoy.features.notifications.NotificationsHelper.CHANNEL_ID;

public class UncompletedTaskApi {

    // нотификации по курсам, которые начали учить или повторять
    // и не закончили

    //
    //.загрузка: актуализация и показ нотификаций
    //.
    //.начали курс:           запланировать отложенную проверку состояния на 30-60 мин
    //.возобновили курс:      запланировать отложенную проверку состояния на 30-60 мин
    //.завершили курс -  TODO по хорошему, проверить - надо ли отменить таймер. СЕЙЧАС НЕ ОТМЕНЯЕТСЯ!
    //.нажали на нотификацию: открываем список незаконченных
    //.смахнули нотификацию:  отложить нотификации
    //


    private static List<LearnCourseMode> sUncompletedCourseModes = null;

    @Inject
    protected Context mContext;
    @Inject
    protected AppPreferences mPreferences;
    @Inject
    protected CoursesRepository mCoursesRepository;

    private UncompletedTaskSettings mS = new UncompletedTaskSettings(
            60 * ScheduleTimeUnit.MINUTE.getMillis(),
            60 * ScheduleTimeUnit.MINUTE.getMillis()
    );

    @Inject
    public UncompletedTaskApi() {
    }

    public void dispose() {
        mContext = null;
        mPreferences = null;
    }

    protected Context getAppContext() {
        return mContext.getApplicationContext();
    }

    protected String getString(@StringRes int id) {
        return mContext.getResources().getString(id);
    }

    /*
    планирует показ нотификаций;
    public void onUncompletedTaskShow(){
        cancelAllAlarmsAndNotifications();
        // запланировать проверку
        //shiftUncompletedChecking();
        planCheckingAlarm(mS.getOpenCourseShiftMillis());
    }*/

    /**
     * onBoot - не используется
     */
    public void checkAndNotifyAboutUncompletedTasks(boolean onBoot) {

        List<LearnCourseEntity> uncompletedCourses = getUncompletedCourses();

        if (uncompletedCourses.size() == 0) {
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
    public List<LearnCourseEntity> getUncompletedCourses() {
        // т.е. те, которые в процессе обучения, или повторения
        return mCoursesRepository.getCoursesByModesNow(
                LearnCourseMode.LEARNING,
                LearnCourseMode.REPEATING);
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

    //интент, заставляющий проверить стейт незавершенных в указанное время и при наличии таковых показать нотификацию
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
