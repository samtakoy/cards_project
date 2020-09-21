package ru.samtakoy.features.notifications.learn_courses;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ru.samtakoy.core.app.utils.DateUtils;
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.data.local.database.room.entities.elements.ScheduleTimeUnit;
import ru.samtakoy.core.data.local.database.room.entities.other.LearnCourseHelper;
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.domain.CoursesRepository;
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.features.notifications.NotificationsHelper;
import ru.samtakoy.features.notifications.NotificationsPlannerService;

public abstract class CoursesApi {

    private static final String TAG = "NCoursesInteractorImpl";

    @Inject
    protected Context mContext;
    @Inject
    protected CoursesRepository mCoursesRepository;

    public CoursesApi() {

    }

    public void dispose() {
        mContext = null;
    }

    @NotNull
    protected abstract LearnCourseMode getLearnCourseMode();

    protected Context getAppContext(){
        return  mContext.getApplicationContext();
    }

    protected String getString(@StringRes int id){
        return mContext.getResources().getString(id);
    }

    protected AlarmManager getAlarmManager() {
        return (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    protected long getCurrentDateLong(){
        return DateUtils.getCurrentTimeLong();
    }

    protected int getShiftMillis(){
        return 20 * ScheduleTimeUnit.MINUTE.getMillis();
    }

    protected Intent getShowUiIntent(){
        return NotificationsPlannerService.getLearnCoursesShowIntent(getAppContext(), getLearnCourseMode());
    }

    protected Intent getShiftIntent(){
        return NotificationsPlannerService.getLearnCoursesShiftIntent(getAppContext(), getLearnCourseMode());
    }

    protected Intent getReSchedulingIntent() {
        return NotificationsPlannerService.getLearnCoursesReSchedulingIntent(getAppContext(), getLearnCourseMode());
    }

    public Long[] getNewLearnCourseIds() {
        return LearnCourseHelper.getLearnCourseIds(getNewLearnCourses());
    }

    public List<LearnCourseEntity> getNewLearnCourses() {
        return mCoursesRepository.getOrderedCoursesLessThan(
                getLearnCourseMode(),
                DateUtils.dateFromDbSerialized(getCurrentDateLong())
        );
    }

    // TODO; транзакцию?
    // TODO сохранять тольк одно поле, а не весь курс
    protected void shiftLearnCourses(LearnCourseMode lcMode, int shiftMillis) {
        long currentDateLong = getCurrentDateLong();
        Date currentDate = DateUtils.dateFromDbSerialized(currentDateLong);
        List<LearnCourseEntity> newLearnCourses = mCoursesRepository.getOrderedCoursesLessThan(lcMode, currentDate);

        long newMinTimeLong = getCurrentDateLong() + shiftMillis;
        Date newMinTime = DateUtils.dateFromDbSerialized(newMinTimeLong);
        Context ctx = getAppContext();

        for (LearnCourseEntity learnCourse : newLearnCourses) {
            learnCourse.setRepeatDate(newMinTime);
            mCoursesRepository.updateCourse(learnCourse);
        }
    }

    /** перепланировать заново нотификации и таймеры курсов */
    protected void rescheduleLearnCourses(LearnCourseMode lcMode, int notificationId, int notificationIconId, String notificationTitle, String notificationText, PendingIntent clickIntent, PendingIntent cancelIntent) {

        MyLog.add("NCoursesInteractorImpl::rescheduleLearnCourses");

        // фиксируем дату для расчетов
        long currentDateLong = getCurrentDateLong();
        Date currentDate = DateUtils.dateFromDbSerialized(currentDateLong);
        List<LearnCourseEntity> newRepeatCourses = mCoursesRepository.getOrderedCoursesLessThan(lcMode, currentDate);

        if(newRepeatCourses.size() > 0){

            MyLog.add("notification show!, current:"+(DateUtils.dateToDbSerialized(currentDate)/1000)
                    +", course:"+(DateUtils.dateToDbSerialized(newRepeatCourses.get(0).getRepeatDate())/1000) );

            // показать нотификацию
            showNotificationForReadyCourses(
                    newRepeatCourses,
                    notificationId, notificationIconId, notificationTitle, notificationText,
                    clickIntent, cancelIntent
            );
            // и отменить все таймеры
            cancelLearnCoursesAlarm();
            return;
        } else {

            MyLog.add("hide notifications");
            hideNotificationForReadyCourses(notificationId);
        }

        List<LearnCourseEntity> futureRepeatCourses = mCoursesRepository.getOrderedCoursesMoreThan(lcMode, currentDate);
        if(futureRepeatCourses.size() == 0){
            MyLog.add("NONE to alarm");
            cancelLearnCoursesAlarm();
        } else {
            MyLog.add("PLAN alarm");

            // TODO тут и во всех сервисах почему-то идет расчет на то, что курсы будут упорядочены по времени повторения
            // хотя такого не было,
            planLearnCoursesAlarm(futureRepeatCourses.get(0));
        }
    }

    /** интент вызываемый при достижении времени старта курса  */
    protected abstract PendingIntent getLearnCoursesAlarmPendingIntent(boolean noCreate);

    /** отменить интент старта курса */
    private void cancelLearnCoursesAlarm(){
        cancelAlarmByPendingIntent(getLearnCoursesAlarmPendingIntent(true));
    }

    /**
     * запланировать интент старта курса
     */
    private void planLearnCoursesAlarm(LearnCourseEntity learnCourse) {
        PendingIntent pIntent = getLearnCoursesAlarmPendingIntent(false);

        MyLog.add(learnCourse.getId() + ": " + String.valueOf(System.currentTimeMillis() / 1000) + "_" + String.valueOf(learnCourse.getRepeatDateUTCMillis() / 1000));
        MyLog.add("planLearnCoursesAlarm, time:" + learnCourse.getRepeatDateDebug(learnCourse));

        long targetTime = SystemClock.elapsedRealtime() + (learnCourse.getRepeatDateUTCMillis() - System.currentTimeMillis());

        //AlarmManagerCompat.setExact(getAlarmManager(), AlarmManager.RTC, learnCourse.getRepeatDateUTCMillis(), pIntent);
        getAlarmManager().set(AlarmManager.ELAPSED_REALTIME, targetTime, pIntent);
    }

    /** показать нотификацию, что курс по времени готов к обучению */
    protected void showNotificationForReadyCourses(
            List<LearnCourseEntity> sortedNewRepeatCourses,
            int notificationId,
            int icon,
            String title,
            String text,
            PendingIntent clickIntent,
            PendingIntent shiftIntent
    ) {
        if(sortedNewRepeatCourses.size() == 0){ return; }

        Notification notification  = new NotificationCompat.Builder(mContext, NotificationsHelper.CHANNEL_ID)
                .setTicker(title)
                .setSmallIcon(icon)

                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(clickIntent)

                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)

                .setDeleteIntent(shiftIntent)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat nManager = NotificationManagerCompat.from(mContext);
        nManager.notify(notificationId, notification);
    }

    /** скрыть нотификацию, что курс по времени готов к обучению */
    protected void hideNotificationForReadyCourses(int notificationId) {
        NotificationManagerCompat nManager = NotificationManagerCompat.from(mContext);
        nManager.cancel(notificationId);
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
