package ru.samtakoy.domain.learncourse

import android.content.Context
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.learncourse.schedule.Schedule
import javax.inject.Inject

// TODO rename to PlanCourseUseCase
internal class CoursesPlannerImpl @Inject constructor() : CoursesPlanner {
    @JvmField
    @Inject
    var mCtx: Context? = null

    @JvmField
    @Inject
    var mCoursesRepository: CoursesRepository? = null

    override fun planUncompletedTasksChecking() {
        // TODO refactor
        // mCtx!!.startService(NotificationsPlannerService.getPlanUncompletedCheckingIntent(mCtx))
    }

    override fun reScheduleLearnCourses() {
        // TODO refactor
        /*
        mCtx!!.startService(
            NotificationsPlannerService.getLearnCoursesReSchedulingIntent(
                mCtx, LearnCourseMode.REPEAT_WAITING
            )
        )*/
    }

    override fun planAdditionalCards(qPackId: Long, errorCardIds: List<Long>, schedule: Schedule) {
        // TODO refactor
        /*
        Completable.fromCallable(
            Callable {
                planAdditionalCards(qPackId, null, errorCardIds, schedule)
                true
            }
        )
            .compose(c_io_mainThread())
            .subscribe(Action {}, Consumer { throwable: Throwable? -> onError(throwable!!) })
         */
   }

    override fun planAdditionalCards(
        qPackId: Long,
        subTitle: String?,
        cardIds: List<Long>,
        restSchedule: Schedule
    ) {
        // TODO refactor
        /*
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
        )*/
    }

    private fun onError(t: Throwable) {
        MyLog.add("CoursesPlannerImpl", t)
    }
}
