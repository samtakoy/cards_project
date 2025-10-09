package ru.samtakoy.features.learncourse.domain

import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import ru.samtakoy.features.views.domain.ViewHistoryItem
import java.util.Date

interface CourseProgressUseCase {
    suspend fun startLearning(learnCourse: LearnCourse): ViewHistoryItem

    suspend fun startRepeating(learnCourse: LearnCourse): ViewHistoryItem

    suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Date)

    suspend fun finishCourseCardsViewingForViewId(viewId: Long, currentTime: Date)
}