package ru.samtakoy.data.learncourseview

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.learncourse.CourseViewRepository
import javax.inject.Inject

internal class CourseViewRepositoryImpl @Inject constructor(
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