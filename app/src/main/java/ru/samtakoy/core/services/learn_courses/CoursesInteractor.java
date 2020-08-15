package ru.samtakoy.core.services.learn_courses;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.List;

import ru.samtakoy.core.screens.log.MyLog;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.elements.ScheduleTimeUnit;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.business.impl.LearnCourseHelper;
import ru.samtakoy.core.services.NotificationsHelper;
import ru.samtakoy.core.services.NotificationsPlannerService;

public abstract class CoursesInteractor {

    private static final String TAG = "NCoursesInteractorImpl";

    protected Context mContext;

    public CoursesInteractor(Context context){
        mContext = context;
    }

    public void dispose(){
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

    protected Intent getReSchedulingIntent(){
        return NotificationsPlannerService.getLearnCoursesReSchedulingIntent(getAppContext(), getLearnCourseMode());
    }

    public Long[] getNewLearnCourseIds() {
        return LearnCourseHelper.getLearnCourseIds(getNewLearnCourses());
    }

    public List<LearnCourse> getNewLearnCourses() {
        return ContentProviderHelper.getCoursesLessThan(
                mContext,
                getLearnCourseMode(),
                DateUtils.dateFromDbSerialized(getCurrentDateLong())
        );
    }

    // TODO; транзакцию?
    // TODO сохранять тольк одно поле, а не весь курс
    protected void shiftLearnCourses(LearnCourseMode lcMode, int shiftMillis) {
        long currentDateLong = getCurrentDateLong();
        Date currentDate = DateUtils.dateFromDbSerialized(currentDateLong);
        List<LearnCourse> newLearnCourses = ContentProviderHelper.getCoursesLessThan( mContext, lcMode, currentDate );

        long newMinTimeLong = getCurrentDateLong() + shiftMillis;
        Date newMinTime = DateUtils.dateFromDbSerialized(newMinTimeLong);
        Context ctx = getAppContext();

        for(LearnCourse learnCourse:newLearnCourses){
            learnCourse.setRepeatDate(newMinTime);
            ContentProviderHelper.saveCourse(ctx, learnCourse);
        }
    }

    /** перепланировать заново нотификации и таймеры курсов */
    protected void rescheduleLearnCourses(LearnCourseMode lcMode, int notificationId, int notificationIconId, String notificationTitle, String notificationText, PendingIntent clickIntent, PendingIntent cancelIntent) {

        MyLog.add("NCoursesInteractorImpl::rescheduleLearnCourses");

        // фиксируем дату для расчетов
        long currentDateLong = getCurrentDateLong();
        Date currentDate = DateUtils.dateFromDbSerialized(currentDateLong);
        List<LearnCourse> newRepeatCourses = ContentProviderHelper.getCoursesLessThan( mContext, lcMode, currentDate );

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

        List<LearnCourse> futureRepeatCourses = ContentProviderHelper.getCoursesMoreThan( mContext, lcMode, currentDate );
        if(futureRepeatCourses.size() == 0){
            MyLog.add("NONE to alarm");
            cancelLearnCoursesAlarm();
        } else {
            MyLog.add("PLAN alarm");
            planLearnCoursesAlarm(futureRepeatCourses.get(0));
        }
    }

    /** интент вызываемый при достижении времени старта курса  */
    protected abstract PendingIntent getLearnCoursesAlarmPendingIntent(boolean noCreate);

    /** отменить интент старта курса */
    private void cancelLearnCoursesAlarm(){
        cancelAlarmByPendingIntent(getLearnCoursesAlarmPendingIntent(true));
    }

    /** запланировать интент старта курса */
    private void planLearnCoursesAlarm(LearnCourse learnCourse){
        PendingIntent pIntent = getLearnCoursesAlarmPendingIntent(false);

        Log.e(TAG, String.valueOf(System.currentTimeMillis()/1000)+"_"+String.valueOf(learnCourse.getRepeatDateUTCMillis()/1000));
        MyLog.add("planLearnCoursesAlarm, time:"+learnCourse.getRepeatDateDebug());

        long targetTime = SystemClock.elapsedRealtime() + (learnCourse.getRepeatDateUTCMillis() - System.currentTimeMillis());
        //AlarmManagerCompat.setExact(getAlarmManager(), AlarmManager.RTC, learnCourse.getRepeatDateUTCMillis(), pIntent);
        getAlarmManager().set(AlarmManager.ELAPSED_REALTIME,  targetTime, pIntent);
    }

    /** показать нотификацию, что курс по времени готов к обучению */
    protected void showNotificationForReadyCourses(
            List<LearnCourse> sortedNewRepeatCourses,
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
