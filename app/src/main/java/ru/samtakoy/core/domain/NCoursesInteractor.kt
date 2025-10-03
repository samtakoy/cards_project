package ru.samtakoy.core.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode

interface NCoursesInteractor {
    suspend fun getCourse(courseId: Long): LearnCourseEntity?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourseEntity?>

    fun getCourseFlowableRx(courseId: Long): Flowable<LearnCourseEntity>

    suspend fun deleteCourse(courseId: Long)

    suspend fun deleteQPackCourses(qPackId: Long)

    suspend fun onAddCardsToCourseFromQPack(qPackId: Long, learnCourseId: Long)

    fun addCardsToCourseRx(learnCourse: LearnCourseEntity, newCardsToAdd: List<Long>): Completable

    suspend fun addCourseForQPack(courseTitle: String, qPackId: Long): LearnCourseEntity

    suspend fun saveCourse(learnCourse: LearnCourseEntity)

    fun getCourseViewIdRx(learnCourseId: Long): Single<Long>

    suspend fun getCourseViewId(learnCourseId: Long): Long?

    fun getCourseLastViewIdAsFlow(learnCourseId: Long): Flow<Long?>

    suspend fun getCourseIdForViewId(viewId: Long): Long?

    suspend fun addCourseView(courseId: Long, viewId: Long)

    suspend fun addNewCourse(newCourse: LearnCourseEntity): LearnCourseEntity

    fun getAllCoursesRx(): Flowable<List<LearnCourseEntity>>

    fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>>

    fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourseEntity>>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourseEntity>>

    fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourseEntity>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourseEntity>>

    fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourseEntity>>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>>
}
