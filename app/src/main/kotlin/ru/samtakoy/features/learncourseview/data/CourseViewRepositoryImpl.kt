package ru.samtakoy.features.learncourseview.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CourseViewRepositoryImpl @Inject constructor(
    private val dao: LearnCourseViewDao
): CourseViewRepository {
    override suspend fun addCourseView(courseId: Long, viewId: Long) {
        dao.add(LearnCourseViewEntity(courseId = courseId, viewId = viewId))
    }

    override suspend fun getCourseLastViewId(courseId: Long): Long? {
        return dao.getCourseLastViewId(courseId = courseId)
    }

    override fun getCourseLastViewIdAsFlow(courseId: Long): Flow<Long> {
        return dao.getCourseLastViewIdAsFlow(courseId = courseId)
    }

    override suspend fun getCourseIdForViewId(viewId: Long): Long? {
        return dao.getCourseIdForViewId(viewId = viewId)
    }
}