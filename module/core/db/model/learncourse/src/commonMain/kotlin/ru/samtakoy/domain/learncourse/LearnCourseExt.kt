@file:OptIn(ExperimentalTime::class)

package ru.samtakoy.domain.learncourse

import ru.samtakoy.common.utils.CollectionUtils
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.domain.learncourse.schedule.Schedule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


fun List<LearnCourseMode>?.listToPrimitiveArray(): IntArray? {
    this ?: return null
    val result = IntArray(this.size)
    this.forEachIndexed { idx, mode ->
        result[idx] = mode.ordinal
    }
    return result
}

fun IntArray?.primitiveArrayToList(): List<LearnCourseMode>? {
    this ?: return null
    return this.toList().map { ordinal ->
        LearnCourseMode.entries[ordinal]
    }
}

// TODO это временный класс, пока не появится domain моделька

fun LearnCourse.finishLearnOrRepeat(currentTime: Instant): LearnCourse {
    return if (restSchedule.isEmpty) {
        this.copy(
            mode = LearnCourseMode.COMPLETED
        )
    } else {
        val currentSystemTimeMillis = DateUtils.getTimeLong(currentTime)
        val transferItem = restSchedule.firstItem!!
        val nextDateLong: Long = transferItem.millis + currentSystemTimeMillis

        MyLog.add(
            id.toString() + ": " + "setNextRepeatDateFrom, cur:" + currentSystemTimeMillis / 1000 + ", newD:" + getRepeatDateDebug(
                this
            )
        )

        this.copy(
            repeatedCount = if (mode != LearnCourseMode.LEARNING) {
                repeatedCount + 1
            } else {
                repeatedCount
            },
            mode = LearnCourseMode.REPEAT_WAITING,
            restSchedule = Schedule(
                restSchedule.items.subList(1, restSchedule.items.size)
            ),
            realizedSchedule = Schedule(
                realizedSchedule.items + transferItem
            ),
            repeatDate = DateUtils.dateFromDbSerialized(nextDateLong)
        )
    }
}

fun makeInitialTodosStatic(cardIds: List<Long>, shuffleCards: Boolean): List<Long> {
    val shuffledIds = if (shuffleCards) {
        CollectionUtils.createShuffledIds(cardIds)
    } else {
        cardIds
    }
    return shuffledIds
}

fun LearnCourse.hasNotInCards(cardIds: List<Long>): Boolean {
    for (cardId in cardIds) {
        if (!this.cardIds.contains(cardId)) {
            return true
        }
    }
    return false
}

fun LearnCourse.getNotInCards(cardIds: List<Long>): List<Long> {
    val result: MutableList<Long> = ArrayList()
    for (cardId in cardIds) {
        if (!this.cardIds.contains(cardId)) {
            result.add(cardId)
        }
    }
    return result
}

fun LearnCourse.hasRealizedSchedule(): Boolean {
    return !realizedSchedule.isEmpty
}

fun LearnCourse.hasRestSchedule(): Boolean {
    return !restSchedule.isEmpty
}

fun LearnCourse.getRepeatDateDebug(learnCourse: LearnCourse): Long {
    return DateUtils.dateToDbSerialized(learnCourse.repeatDate) / 1000
}

fun LearnCourse.getRepeatDateUTCMillis(): Long {
    return DateUtils.dateToDbSerialized(repeatDate)
}

// ======================================================================
// ======================================================================

// TODO временно
fun LearnCourse.getDynamicTitle(): String {
    // активные
    if (mode == LearnCourseMode.LEARNING || mode == LearnCourseMode.REPEATING) {
        return "(a)" + title
    } else if (mode == LearnCourseMode.LEARN_WAITING || mode == LearnCourseMode.REPEAT_WAITING) {
        if (getMillisToStart() <= 0) {
            return "(!)" + title
        }
    }
    return title
}

fun LearnCourse.getMillisToStart(): Long {
    return DateUtils.getMillisTo(repeatDate)
}