package ru.samtakoy.core.domain.impl

import android.content.Context
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.samtakoy.R
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.data.local.reps.CoursesRepository
import ru.samtakoy.core.domain.CoursesPlanner
import ru.samtakoy.core.domain.utils.c_io_mainThread
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.features.notifications.NotificationsConst
import ru.samtakoy.features.notifications.NotificationsPlannerService
import java.text.MessageFormat
import java.util.concurrent.Callable
import javax.inject.Inject

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

    override fun planAdditionalCards(qPackId: Long, errorCardIds: MutableList<Long>, schedule: Schedule) {
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
        cardIds: MutableList<Long>,
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

        // createNewAdditional
        val lc = LearnCourseEntity.initNew(
            qPackId = qPackId,
            courseType = CourseType.ADDITIONAL,
            title = title,
            mode = LearnCourseMode.LEARN_WAITING,
            cardIds = ArrayList(cardIds),
            restSchedule = restSchedule,
            repeatDate = DateUtils.dateFromDbSerialized(
                DateUtils.getCurrentTimeLong() + NotificationsConst.NEW_COURSE_LEARN_DEFAULT_MILLIS_DELTA
            )
        )
        mCoursesRepository!!.addNewCourseNow(lc)

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
