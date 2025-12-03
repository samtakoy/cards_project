package ru.samtakoy.oldlegacy.features.notifications.learn_courses

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.SystemClock
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking
import ru.samtakoy.R
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.domain.learncourse.LearnCourse
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit
import ru.samtakoy.oldlegacy.features.notifications.NotificationsConst
import ru.samtakoy.oldlegacy.features.notifications.NotificationsHelper
import ru.samtakoy.oldlegacy.features.notifications.NotificationsPlannerService
import ru.samtakoy.oldlegacy.features.preferences.data.AppPreferences
import java.util.EnumSet

// TODO тут все пересмотреть и переделать
class UncompletedTaskApi(
    private val mContext: Context,
    private val mPreferences: AppPreferences,
    private val  mCoursesRepository: CoursesRepository
) {
    private val mS = UncompletedTaskSettings(
        60 * ScheduleTimeUnit.MINUTE.getMillis(),
        60 * ScheduleTimeUnit.MINUTE.getMillis()
    )

    protected val appContext: Context?
        get() = mContext.getApplicationContext()

    protected fun getString(@StringRes id: Int): String {
        return mContext.getResources().getString(id)
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
    fun checkAndNotifyAboutUncompletedTasks(onBoot: Boolean) {
        val uncompletedCourses = this.uncompletedCourses

        if (uncompletedCourses.size == 0) {
            // отменить все таймеры и нотификации
            cancelAllAlarmsAndNotifications()
            return
        }

        if (!onBoot) {
        }

        showNotification()
    }

    private fun showNotification() {
        // NOTIFICATION_ID_UNCOMPLETED_TASKS;
        val notificationTitle = getString(R.string.notifications_uncompleted_title)
        val notificationText = getString(R.string.notifications_uncompleted_text)

        val clickIntent = PendingIntent.getService(
            this.appContext,
            NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_CLICK,
            NotificationsPlannerService.Companion.getShowUncompletedTasksIntent(this.appContext),
            PendingIntent.FLAG_IMMUTABLE
        )
        val shiftIntent = PendingIntent.getService(
            this.appContext,
            NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_CANCEL,
            NotificationsPlannerService.Companion.getSchiftUncompletedCheckingIntent(this.appContext),
            PendingIntent.FLAG_IMMUTABLE
        )

        // показать нотификацию
        val notification = NotificationCompat.Builder(mContext, NotificationsHelper.CHANNEL_ID)
            .setTicker(notificationTitle)
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(clickIntent)
            .setDeleteIntent(shiftIntent)
            .setAutoCancel(true)
            .build()
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO refactor
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(mContext).notify(
            NotificationsConst.NOTIFICATION_ID_UNCOMPLETED_TASKS,
            notification
        )
    }

    val uncompletedCourseModes: List<LearnCourseMode>
        get() {
            if (sUncompletedCourseModes == null) {
                sUncompletedCourseModes = ArrayList<LearnCourseMode>(
                    EnumSet.of<LearnCourseMode?>(LearnCourseMode.LEARNING, LearnCourseMode.REPEATING)
                )
            }
            return sUncompletedCourseModes!!
        }

    val uncompletedCourses: List<LearnCourse>
        get() = // т.е. те, которые в процессе обучения, или повторения
            runBlocking {
                mCoursesRepository.getCoursesByModesNow(
                    LearnCourseMode.LEARNING,
                    LearnCourseMode.REPEATING
                )
            }

    private fun cancelAllAlarmsAndNotifications() {
        cancelNotification()
        cancelAlarmByPendingIntent(getAlarmPendingIntent(true))
    }

    private fun cancelNotification() {
        NotificationManagerCompat.from(mContext).cancel(NotificationsConst.NOTIFICATION_ID_UNCOMPLETED_TASKS)
    }

    /** сдвинуть проверку неоконченных  */
    fun shiftUncompletedChecking() {
        cancelAllAlarmsAndNotifications()

        mPreferences.setUncompletedNotificationMinUtc(
            DateUtils.getDateAfterCurrentLong(mS.shiftMillis.toLong())
        )

        planCheckingAlarm(mS.shiftMillis)
    }

    private fun planCheckingAlarm(shiftMillis: Int) {
        val pIntent = getAlarmPendingIntent(false)
        val aManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val targetTime = SystemClock.elapsedRealtime() + shiftMillis
        //AlarmManagerCompat.setExact(aManager, AlarmManager.ELAPSED_REALTIME,  targetTime, pIntent);
        aManager.set(AlarmManager.ELAPSED_REALTIME, targetTime, pIntent)
    }

    //интент, заставляющий проверить стейт незавершенных в указанное время и при наличии таковых показать нотификацию
    private fun getAlarmPendingIntent(noCreate: Boolean): PendingIntent {
        return PendingIntent.getService(
            this.appContext,
            NotificationsConst.REQ_CODE_UNCOMPLETED_TASKS_ALARM,
            NotificationsPlannerService.Companion.getCheckUncompletedTasksIntent(
                this.appContext
            ),
            if (noCreate) PendingIntent.FLAG_NO_CREATE else 0
        )
    }

    /** отменить AlarmManager по PendingIntent  */
    protected fun cancelAlarmByPendingIntent(pIntent: PendingIntent?) {
        if (pIntent != null) {
            val aManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            aManager.cancel(pIntent)
            pIntent.cancel()
        }
    }

    companion object {
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
        private var sUncompletedCourseModes: List<LearnCourseMode>? = null
    }
}
