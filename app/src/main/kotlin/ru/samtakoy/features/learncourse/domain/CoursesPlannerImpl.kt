package ru.samtakoy.features.learncourse.domain

import android.content.Context
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.coroutines.runBlocking
import ru.samtakoy.R
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.core.domain.utils.c_io_mainThread
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.learncourse.CourseType
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.features.notifications.NotificationsConst
import ru.samtakoy.features.notifications.NotificationsPlannerService
import java.text.MessageFormat
import java.util.concurrent.Callable
import javax.inject.Inject

// TODO rename to PlanCourseUseCase
class CoursesPlannerImpl @Inject constructor() : CoursesPlanner {
    @JvmField
    @Inject
    var mCtx: Context? = null

    @JvmField
    @Inject
    var mCoursesRepository: CoursesRepository? = null

    override fun planUncompletedTasksChecking() {
        mCtx!!.startService(NotificationsPlannerService.getPlanUncompletedCheckingIntent(mCtx))
    }

    override fun reScheduleLearnCourses() {
        mCtx!!.startService(
            NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                mCtx, LearnCourseMode.REPEAT_WAITING
            )
        )
    }

    override fun planAdditionalCards(qPackId: Long, errorCardIds: List<Long>, schedule: Schedule) {
        Completable.fromCallable(
            Callable {
                planAdditionalCards(qPackId, null, errorCardIds, schedule)
                true
            }
        )
            .compose(c_io_mainThread())
            .subscribe(Action {}, Consumer { throwable: Throwable? -> onError(throwable!!) })
    }

    override fun planAdditionalCards(
        qPackId: Long,
        subTitle: String?,
        cardIds: List<Long>,
        restSchedule: Schedule
    ) {
        var title: String

        if (subTitle == null) {
            title = mCtx!!.getResources().getString(R.string.additional_learn_course_title)
            title = MessageFormat.format(title, cardIds.size)
        } else {
            title = mCtx!!.getResources().getString(R.string.additional_learn_course_title_with_subtitle)
            title = MessageFormat.format(title, cardIds.size, subTitle)
        }

        runBlocking {
            mCoursesRepository!!.addNewCourse(
                qPackId = qPackId,
                courseType = CourseType.ADDITIONAL,
                title = title,
                mode = LearnCourseMode.LEARN_WAITING,
                cardIds = ArrayList(cardIds),
                restSchedule = restSchedule,
                repeatDate = DateUtils.dateFromDbSerialized(
                    DateUtils.currentTimeLong + NotificationsConst.NEW_COURSE_LEARN_DEFAULT_MILLIS_DELTA
                )
            )
        }

        mCtx!!.startService(
            NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                mCtx, LearnCourseMode.LEARN_WAITING
            )
        )
    }

    private fun onError(t: Throwable) {
        MyLog.add("CoursesPlannerImpl", t)
    }
}
