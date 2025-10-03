package ru.samtakoy.core.data.local.reps.impl

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.dao.LearnCourseViewDao
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseViewEntity
import ru.samtakoy.core.data.local.reps.CourseViewRepository
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

    override fun getCourseLastViewIdAsFlow(courseId: Long): Flow<Long?> {
        return dao.getCourseLastViewIdAsFlow(courseId = courseId)
    }

    override suspend fun getCourseIdForViewId(viewId: Long): Long? {
        return dao.getCourseIdForViewId(viewId = viewId)
    }
}