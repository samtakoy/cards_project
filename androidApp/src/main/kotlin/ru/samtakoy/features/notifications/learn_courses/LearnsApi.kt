package ru.samtakoy.features.notifications.learn_courses

import android.app.PendingIntent
import android.content.Context
import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.features.notifications.NotificationsConst

// TODO тут все пересмотреть и переделать
class LearnsApi(
    context: Context,
    resources: Resources,
    coursesRepository: CoursesRepository,
) : CoursesApi(
    context,
    resources,
    coursesRepository,
) {
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
        val notificationTitle = resources.getString(R.string.notifications_learn_waitings_title)
        val notificationText = resources.getString(R.string.notifications_learn_waitings_text)

        val clickIntent =
            PendingIntent.getService(
                context,
                NotificationsConst.REQ_CODE_NEW_LEARNINGS_CLICK,
                showUiIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        val cancelIntent =
            PendingIntent.getService(
                context,
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
            context,
            NotificationsConst.REQ_CODE_NEW_LEARNINGS_ALARM,
            reSchedulingIntent,
            if (noCreate) PendingIntent.FLAG_NO_CREATE else 0
        )
    }
}
