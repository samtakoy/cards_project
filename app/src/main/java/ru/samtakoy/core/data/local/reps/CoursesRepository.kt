package ru.samtakoy.core.data.local.reps

import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import java.util.Date

interface CoursesRepository {
    suspend fun getCourse(learnCourseId: Long): LearnCourseEntity?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourseEntity?>

    fun getCourseFlowableRx(learnCourseId: Long): Flowable<LearnCourseEntity>

    fun getCourseByMode(mode: LearnCourseMode): LearnCourseEntity

    suspend fun updateCourse(course: LearnCourseEntity): Boolean

    suspend fun deleteCourse(courseId: Long)

    suspend fun deleteQPackCourses(qPackId: Long)

    // TODO TEMP
    fun addNewCourseNow(newCourse: LearnCourseEntity): LearnCourseEntity

    suspend fun addNewCourse(newCourse: LearnCourseEntity): LearnCourseEntity

    fun getAllCoursesRx(): Flowable<List<LearnCourseEntity>>

    fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>>

    suspend fun getAllCourses(): List<LearnCourseEntity>

    fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourseEntity>>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourseEntity>>

    fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourseEntity>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourseEntity>>

    fun getCoursesByModes(vararg mode: LearnCourseMode): Flowable<List<LearnCourseEntity>>

    fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourseEntity>

    fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourseEntity>>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>>

    fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourseEntity>

    fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourseEntity>
}
