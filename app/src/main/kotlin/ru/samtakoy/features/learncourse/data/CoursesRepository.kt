package ru.samtakoy.features.learncourse.data

import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.learncourse.domain.model.CourseType
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import ru.samtakoy.features.learncourse.domain.model.LearnCourseMode
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import java.util.Date

interface CoursesRepository {
    suspend fun getCourse(learnCourseId: Long): LearnCourse?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?>

    fun getCourseFlowableRx(learnCourseId: Long): Flowable<LearnCourse>

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

    fun getAllCoursesRx(): Flowable<List<LearnCourse>>

    fun getAllCoursesAsFlow(): Flow<List<LearnCourse>>

    suspend fun getAllCourses(): List<LearnCourse>

    fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourse>>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>>

    fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourse>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>>

    fun getCoursesByModes(vararg mode: LearnCourseMode): Flowable<List<LearnCourse>>

    fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourse>

    fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourse>>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>>

    fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourse>

    fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourse>
}
