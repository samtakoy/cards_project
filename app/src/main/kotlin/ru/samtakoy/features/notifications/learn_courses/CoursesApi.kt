package ru.samtakoy.features.notifications.learn_courses

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.domain.learncourse.getRepeatDateDebug
import ru.samtakoy.domain.learncourse.getRepeatDateUTCMillis
import ru.samtakoy.data.learncourse.utils.LearnCourseHelper
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.learncourse.LearnCourse
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit
import ru.samtakoy.features.notifications.NotificationsHelper
import ru.samtakoy.features.notifications.NotificationsPlannerService
import kotlin.Array
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import javax.inject.Inject

abstract class CoursesApi {
    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mResources: Resources

    @Inject
    lateinit var mCoursesRepository: CoursesRepository

    fun dispose() {

    }

    protected abstract val learnCourseMode: LearnCourseMode

    protected val appContext: Context?
        get() = mContext.getApplicationContext()

    protected val alarmManager: AlarmManager?
        get() = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    protected val currentDateLong: Long
        get() = DateUtils.currentTimeLong

    protected val shiftMillis: Int
        get() = 20 * ScheduleTimeUnit.MINUTE.getMillis()

    protected val showUiIntent: Intent
        get() = NotificationsPlannerService.getLearnCoursesShowIntent(this.appContext, this.learnCourseMode)

    protected val shiftIntent: Intent
        get() = NotificationsPlannerService.getLearnCoursesShiftIntent(this.appContext, this.learnCourseMode)

    protected val reSchedulingIntent: Intent
        get() = NotificationsPlannerService.getLearnCoursesReSchedulingIntent(this.appContext, this.learnCourseMode)

    val newLearnCourseIds: Array<Long>
        get() = LearnCourseHelper.getLearnCourseIds(this.newLearnCourses)

    val newLearnCourses: List<LearnCourse>
        get() = mCoursesRepository.getOrderedCoursesLessThan(
            this.learnCourseMode,
            DateUtils.dateFromDbSerialized(this.currentDateLong)
        )

    // TODO; транзакцию?
    // TODO сохранять тольк одно поле, а не весь курс
    protected fun shiftLearnCourses(lcMode: LearnCourseMode, shiftMillis: Int) {
        TODO()
        /*
        val currentDateLong = this.currentDateLong
        val currentDate = DateUtils.dateFromDbSerialized(currentDateLong)
        val newLearnCourses: List<LearnCourse> =
            mCoursesRepository.getOrderedCoursesLessThan(lcMode, currentDate)

        val newMinTimeLong = this.currentDateLong + shiftMillis
        val newMinTime = DateUtils.dateFromDbSerialized(newMinTimeLong)

        for (learnCourse in newLearnCourses) {

            mCoursesRepository.updateCourse(
                learnCourse.copy(
                    repeatDate = newMinTime
                )
            )
        }*/
    }

    /** перепланировать заново нотификации и таймеры курсов  */
    protected fun rescheduleLearnCourses(
        lcMode: LearnCourseMode,
        notificationId: Int,
        notificationIconId: Int,
        notificationTitle: String?,
        notificationText: String?,
        clickIntent: PendingIntent?,
        cancelIntent: PendingIntent?
    ) {
        MyLog.add("NCoursesInteractorImpl::rescheduleLearnCourses")

        // фиксируем дату для расчетов
        val currentDateLong = this.currentDateLong
        val currentDate = DateUtils.dateFromDbSerialized(currentDateLong)
        val newRepeatCourses: List<LearnCourse> =
            mCoursesRepository.getOrderedCoursesLessThan(lcMode, currentDate)

        if (newRepeatCourses.size > 0) {
            MyLog.add(
                ("notification show!, current:" + (DateUtils.dateToDbSerialized(currentDate) / 1000)
                    + ", course:" + (DateUtils.dateToDbSerialized(newRepeatCourses.get(0).repeatDate) / 1000))
            )

            // показать нотификацию
            showNotificationForReadyCourses(
                newRepeatCourses,
                notificationId, notificationIconId, notificationTitle, notificationText,
                clickIntent, cancelIntent
            )
            // и отменить все таймеры
            cancelLearnCoursesAlarm()
            return
        } else {
            MyLog.add("hide notifications")
            hideNotificationForReadyCourses(notificationId)
        }

        val futureRepeatCourses: List<LearnCourse?> =
            mCoursesRepository.getOrderedCoursesMoreThan(lcMode, currentDate)
        if (futureRepeatCourses.size == 0) {
            MyLog.add("NONE to alarm")
            cancelLearnCoursesAlarm()
        } else {
            MyLog.add("PLAN alarm")

            // TODO тут и во всех сервисах почему-то идет расчет на то, что курсы будут упорядочены по времени повторения
            // хотя такого не было,
            planLearnCoursesAlarm(futureRepeatCourses.get(0)!!)
        }
    }

    /** интент вызываемый при достижении времени старта курса   */
    protected abstract fun getLearnCoursesAlarmPendingIntent(noCreate: Boolean): PendingIntent

    /** отменить интент старта курса  */
    private fun cancelLearnCoursesAlarm() {
        cancelAlarmByPendingIntent(getLearnCoursesAlarmPendingIntent(true))
    }

    /**
     * запланировать интент старта курса
     */
    private fun planLearnCoursesAlarm(learnCourse: LearnCourse) {
        val pIntent = getLearnCoursesAlarmPendingIntent(false)

        MyLog.add(
            learnCourse.id.toString() + ": " + (System.currentTimeMillis() / 1000).toString() + "_" + (
                learnCourse.getRepeatDateUTCMillis() / 1000
            ).toString()
        )
        MyLog.add("planLearnCoursesAlarm, time:" + learnCourse.getRepeatDateDebug(learnCourse))

        val targetTime: Long =
            SystemClock.elapsedRealtime() + (learnCourse.getRepeatDateUTCMillis() - System.currentTimeMillis())

        //AlarmManagerCompat.setExact(getAlarmManager(), AlarmManager.RTC, learnCourse.getRepeatDateUTCMillis(), pIntent);
        this.alarmManager!!.set(AlarmManager.ELAPSED_REALTIME, targetTime, pIntent)
    }

    /** показать нотификацию, что курс по времени готов к обучению  */
    protected fun showNotificationForReadyCourses(
        sortedNewRepeatCourses: List<LearnCourse>,
        notificationId: Int,
        icon: Int,
        title: String?,
        text: String?,
        clickIntent: PendingIntent?,
        shiftIntent: PendingIntent?
    ) {
        if (sortedNewRepeatCourses.size == 0) {
            return
        }

        val notification = NotificationCompat.Builder(mContext, NotificationsHelper.CHANNEL_ID)
            .setTicker(title)
            .setSmallIcon(icon)

            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(clickIntent)

            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)

            .setDeleteIntent(shiftIntent)
            .setAutoCancel(true)
            .build()
        val nManager = NotificationManagerCompat.from(mContext)
        // TODO
        // nManager.notify(notificationId, notification)
    }

    /** скрыть нотификацию, что курс по времени готов к обучению  */
    protected fun hideNotificationForReadyCourses(notificationId: Int) {
        val nManager = NotificationManagerCompat.from(mContext)
        nManager.cancel(notificationId)
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
        private const val TAG = "NCoursesInteractorImpl"
    }
}
