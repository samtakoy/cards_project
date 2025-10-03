package ru.samtakoy.core.data.local.database.room.entities

import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.core.utils.CollectionUtils
import java.util.ArrayDeque
import java.util.Date
import java.util.Deque

// TODO это временный класс, пока не появится domain моделька

fun LearnCourseEntity.finishLearnOrRepeat(currentTime: Date): LearnCourseEntity {
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
                restSchedule.mItems.subList(1, restSchedule.mItems.size)
            ),
            realizedSchedule = Schedule(
                realizedSchedule.mItems + transferItem
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
    val todoCardIds = createNewEmptyTodoIds()
    for (id in shuffledIds) {
        todoCardIds.addLast(id)
    }
    return todoCardIds.toList()
}

private fun createNewEmptyTodoIds(): Deque<Long> {
    return ArrayDeque()
}

fun LearnCourseEntity.hasNotInCards(cardIds: List<Long>): Boolean {
    for (cardId in cardIds) {
        if (!this.cardIds.contains(cardId)) {
            return true
        }
    }
    return false
}

fun LearnCourseEntity.getNotInCards(cardIds: List<Long>): List<Long> {
    val result: MutableList<Long> = ArrayList()
    for (cardId in cardIds) {
        if (!this.cardIds.contains(cardId)) {
            result.add(cardId)
        }
    }
    return result
}

fun LearnCourseEntity.hasRealizedSchedule(): Boolean {
    return !realizedSchedule.isEmpty
}

fun LearnCourseEntity.hasRestSchedule(): Boolean {
    return restSchedule != null && !restSchedule.isEmpty
}

fun LearnCourseEntity.getRepeatDateDebug(learnCourse: LearnCourseEntity): Long {
    return DateUtils.dateToDbSerialized(learnCourse.repeatDate) / 1000
}

fun LearnCourseEntity.getRepeatDateUTCMillis(): Long {
    return DateUtils.dateToDbSerialized(repeatDate)
}

// ======================================================================
// ======================================================================

// TODO временно
fun LearnCourseEntity.getDynamicTitle(): String {
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

fun LearnCourseEntity.getMillisToStart(): Long {
    return DateUtils.getMillisTo(repeatDate)
}