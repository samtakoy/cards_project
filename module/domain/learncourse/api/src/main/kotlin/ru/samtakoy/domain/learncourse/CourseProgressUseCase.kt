package ru.samtakoy.domain.learncourse

import ru.samtakoy.domain.view.ViewHistoryItem
import java.util.Date

interface CourseProgressUseCase {
    suspend fun startLearning(learnCourse: LearnCourse): ViewHistoryItem

    suspend fun startRepeating(learnCourse: LearnCourse): ViewHistoryItem

    suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Date)

    suspend fun finishCourseCardsViewingForViewId(viewId: Long, currentTime: Date)
}