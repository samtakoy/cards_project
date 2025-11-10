package ru.samtakoy.domain.learncourse

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.learncourse.schedule.Schedule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface NCoursesInteractor {
    suspend fun getCourse(courseId: Long): LearnCourse?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?>

    suspend fun deleteCourse(courseId: Long)

    suspend fun deleteQPackCourses(qPackId: Long)

    suspend fun onAddCardsToCourseFromQPack(qPackId: Long, learnCourseId: Long)

    suspend fun addCourseForQPack(courseTitle: String, qPackId: Long): LearnCourse

    suspend fun saveCourse(learnCourse: LearnCourse)

    suspend fun getCourseViewId(learnCourseId: Long): Long?

    fun getCourseLastViewIdAsFlow(learnCourseId: Long): Flow<Long>

    suspend fun getCourseIdForViewId(viewId: Long): Long?

    suspend fun addCourseView(courseId: Long, viewId: Long)

    suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse

    @OptIn(ExperimentalTime::class)
    suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Instant?
    ): LearnCourse

    fun getAllCoursesAsFlow(): Flow<List<LearnCourse>>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>>
}
