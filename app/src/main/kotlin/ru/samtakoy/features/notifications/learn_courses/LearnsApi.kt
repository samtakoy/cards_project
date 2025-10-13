package ru.samtakoy.features.notifications.learn_courses

import android.app.PendingIntent
import ru.samtakoy.R
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.features.notifications.NotificationsConst
import javax.inject.Inject

class LearnsApi @Inject constructor() : CoursesApi() {
    override val learnCourseMode: LearnCourseMode
        get() = LearnCourseMode.LEARN_WAITING

    fun shiftLearnCourses() {
        shiftLearnCourses(learnCourseMode, shiftMillis)
        rescheduleNewLearnCourses()
    }

    /** обновить нотификации и таймеры напоминания  */
    fun rescheduleNewLearnCourses() {
        val lcMode = learnCourseMode
        val notificationId = NotificationsConst.NOTIFICATION_ID_NEW_LEARNINGS_AVAILABLE
        val notificationIconId = android.R.drawable.ic_menu_report_image
        val notificationTitle = mResources.getString(R.string.notifications_learn_waitings_title)
        val notificationText = mResources.getString(R.string.notifications_learn_waitings_text)

        val clickIntent =
            PendingIntent.getService(
                mContext,
                NotificationsConst.REQ_CODE_NEW_LEARNINGS_CLICK,
                showUiIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        val cancelIntent =
            PendingIntent.getService(
                mContext,
                NotificationsConst.REQ_CODE_NEW_LEARNINGS_CANCEL,
                shiftIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        rescheduleLearnCourses(
            lcMode,
            notificationId,
            notificationIconId,
            notificationTitle,
            notificationText,
            clickIntent,
            cancelIntent
        )
    }

    override fun getLearnCoursesAlarmPendingIntent(noCreate: Boolean): PendingIntent {
        return PendingIntent.getService(
            mContext,
            NotificationsConst.REQ_CODE_NEW_LEARNINGS_ALARM,
            reSchedulingIntent,
            if (noCreate) PendingIntent.FLAG_NO_CREATE else 0
        )
    }
}
