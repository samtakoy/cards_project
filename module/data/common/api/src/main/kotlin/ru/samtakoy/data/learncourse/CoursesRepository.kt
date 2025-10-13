package ru.samtakoy.data.learncourse

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.learncourse.CourseType
import ru.samtakoy.domain.learncourse.LearnCourse
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.domain.learncourse.schedule.Schedule
import java.util.Date

interface CoursesRepository {
    suspend fun getCourse(learnCourseId: Long): LearnCourse?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?>

    suspend fun updateCourse(course: LearnCourse): Boolean

    suspend fun deleteCourse(courseId: Long)

    suspend fun deleteQPackCourses(qPackId: Long)

    // TODO TEMP
    fun addNewCourseSync(newCourse: LearnCourse): LearnCourse

    suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Date?
    ): LearnCourse

    suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse

    fun getAllCoursesAsFlow(): Flow<List<LearnCourse>>

    suspend fun getAllCourses(): List<LearnCourse>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>>

    fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourse>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>>

    fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourse>

    fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourse>
}
