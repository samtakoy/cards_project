package ru.samtakoy.core.data.local.reps

import kotlinx.coroutines.flow.Flow

interface CourseViewRepository {
    suspend fun addCourseView(courseId: Long, viewId: Long)
    suspend fun getCourseLastViewId(courseId: Long): Long?
    fun getCourseLastViewIdAsFlow(courseId: Long): Flow<Long?>
    suspend fun getCourseIdForViewId(viewId: Long): Long?
}