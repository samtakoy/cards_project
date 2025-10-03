package ru.samtakoy.core.presentation.courses.info.vm

import androidx.annotation.StringRes
import ru.samtakoy.R
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.TimeViewUtils
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.getMillisToStart
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.features.views.domain.ViewHistoryItem
import java.text.MessageFormat
import javax.inject.Inject

internal interface CourseInfoViewStateMapper {
    fun mapData(
        learnCourse: LearnCourseEntity,
        lastView: ViewHistoryItem?
    ): CourseInfoViewModel.State.Type.Data
}

internal class CourseInfoViewStateMapperImpl @Inject constructor(
    private val resources: Resources
): CourseInfoViewStateMapper {

    override fun mapData(
        learnCourse: LearnCourseEntity,
        lastView: ViewHistoryItem?
    ): CourseInfoViewModel.State.Type.Data {
        return CourseInfoViewModel.State.Type.Data(
            titleText = learnCourse.title,
            cardsCountText = MessageFormat.format(
                resources.getString(R.string.course_info_card_count),
                learnCourse.cardIds.size
            ),
            statusString = getStatusString(
                learnCourse = learnCourse,
                lastView = lastView
            ),
            scheduleButtonText = getScheduleButtonText(learnCourse),
            actionButtonText = getActionButtonText(
                learnCourse = learnCourse,
                lastView = lastView
            )
        )
    }

    private fun getScheduleButtonText(learnCourse: LearnCourseEntity): String {
        if (learnCourse.mode == LearnCourseMode.COMPLETED) {
            return resources.getString(R.string.course_info_schedule_is_completed)
        }
        if (learnCourse.mode == LearnCourseMode.PREPARING) {
            if (learnCourse.restSchedule.isEmpty) {
                return resources.getString(R.string.course_info_schedule_is_empty)
            } else {
                return learnCourse.restSchedule.toStringView(resources)
            }
        }
        if (learnCourse.mode == LearnCourseMode.REPEATING || learnCourse.mode == LearnCourseMode.REPEAT_WAITING) {
            return learnCourse.restSchedule.toStringViewWithPrev(resources, learnCourse.realizedSchedule)
        }
        return learnCourse.restSchedule.toStringView(resources)
    }

    private fun getStatusString(learnCourse: LearnCourseEntity, lastView: ViewHistoryItem?): String {
        val resString: String

        when (learnCourse.mode) {
            LearnCourseMode.PREPARING -> return resources.getString(R.string.course_info_status_preparing)
            LearnCourseMode.LEARN_WAITING -> {
                resString = resources.getString(R.string.course_info_status_learn_waiting)
                return MessageFormat.format(
                    resString,
                    TimeViewUtils.getTimeView(
                        learnCourse.getMillisToStart().toInt()
                    )
                )
            }
            LearnCourseMode.LEARNING -> {
                resString = resources.getString(R.string.course_info_status_learning)
                return MessageFormat.format(
                    resString,
                    getViewedCardsCount(lastView).toString() + "/" + learnCourse.cardIds.size
                )
            }
            LearnCourseMode.REPEAT_WAITING -> {
                resString = resources.getString(R.string.course_info_status_repeat_waiting)
                return MessageFormat.format(
                    resString,
                    TimeViewUtils.getTimeView(
                        learnCourse.getMillisToStart().toInt()
                    )
                )
            }
            LearnCourseMode.REPEATING -> {
                resString = resources.getString(R.string.course_info_status_repeating)
                return MessageFormat.format(
                    resString,
                    getViewedCardsCount(lastView).toString() + "/" + learnCourse.cardIds.size
                )
            }
            LearnCourseMode.COMPLETED -> return resources.getString(R.string.course_info_status_completed)
            else -> return "..."
        }
    }

    private fun getActionButtonText(learnCourse: LearnCourseEntity, lastView: ViewHistoryItem?): String {
        @StringRes val resId: Int
        when (learnCourse.mode) {
            LearnCourseMode.PREPARING -> resId = R.string.course_info_btn_complete_preparing
            LearnCourseMode.LEARN_WAITING -> resId = R.string.course_info_btn_start_learning
            LearnCourseMode.LEARNING -> {
                if (getViewedCardsCount(lastView) > 0) {
                    resId = R.string.course_info_btn_continue_learning
                } else {
                    resId = R.string.course_info_btn_learn
                }
            }
            LearnCourseMode.REPEAT_WAITING -> resId = R.string.course_info_btn_repeat_now
            LearnCourseMode.REPEATING -> {
                if (getViewedCardsCount(lastView) > 0) {
                    resId = R.string.course_info_btn_continue_repeating
                } else {
                    resId = R.string.course_info_btn_repeat
                }
            }
            LearnCourseMode.COMPLETED -> return resources.getString(R.string.course_info_btn_repeat)
            else -> return "..."
        }
        return resources.getString(resId)
    }

    private fun getViewedCardsCount(lastView: ViewHistoryItem?): Int {
        return lastView?.viewedCardIds?.size ?: 0
    }
}