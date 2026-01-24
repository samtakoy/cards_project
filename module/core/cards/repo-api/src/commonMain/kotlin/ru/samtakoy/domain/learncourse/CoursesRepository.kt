package ru.samtakoy.domain.learncourse

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.learncourse.schedule.Schedule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface CoursesRepository {
    suspend fun getCourse(learnCourseId: Long): LearnCourse?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?>

    suspend fun updateCourse(course: LearnCourse): Boolean

    suspend fun deleteCourse(courseId: Long)

    suspend fun deleteQPackCourses(qPackId: Long)

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

    suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse

    fun getAllCoursesAsFlow(): Flow<List<LearnCourse>>

    suspend fun getAllCourses(): List<LearnCourse>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>>

    suspend fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourse>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>>

    @OptIn(ExperimentalTime::class)
    suspend fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Instant): List<LearnCourse>

    @OptIn(ExperimentalTime::class)
    suspend fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Instant): List<LearnCourse>
}
