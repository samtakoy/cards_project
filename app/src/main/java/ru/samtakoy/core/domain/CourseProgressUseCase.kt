package ru.samtakoy.core.domain

import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.features.views.domain.ViewHistoryItem
import java.util.Date

interface CourseProgressUseCase {
    suspend fun startLearning(learnCourse: LearnCourseEntity): ViewHistoryItem

    suspend fun startRepeating(learnCourse: LearnCourseEntity): ViewHistoryItem

    suspend fun finishCourseCardsViewing(courseId: Long, currentTime: Date)

    suspend fun finishCourseCardsViewingForViewId(viewId: Long, currentTime: Date)
}