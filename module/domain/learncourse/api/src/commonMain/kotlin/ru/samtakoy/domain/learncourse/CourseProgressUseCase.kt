package ru.samtakoy.domain.learncourse

import ru.samtakoy.domain.view.ViewHistoryItem
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface CourseProgressUseCase {
    suspend fun startLearning(learnCourse: LearnCourse): ViewHistoryItem

    suspend fun startRepeating(learnCourse: LearnCourse): ViewHistoryItem

    @OptIn(ExperimentalTime::class)
    suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Instant)

    @OptIn(ExperimentalTime::class)
    suspend fun finishCourseCardsViewingForViewId(viewId: Long, currentTime: Instant)
}