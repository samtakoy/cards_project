package ru.samtakoy.features.learncourse.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.learncourse.domain.model.CourseType
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import ru.samtakoy.features.learncourse.domain.model.LearnCourseMode
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import java.util.Date

interface NCoursesInteractor {
    suspend fun getCourse(courseId: Long): LearnCourse?

    fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?>

    fun getCourseFlowableRx(courseId: Long): Flowable<LearnCourse>

    suspend fun deleteCourse(courseId: Long)

    suspend fun deleteQPackCourses(qPackId: Long)

    suspend fun onAddCardsToCourseFromQPack(qPackId: Long, learnCourseId: Long)

    fun addCardsToCourseRx(learnCourse: LearnCourse, newCardsToAdd: List<Long>): Completable

    suspend fun addCourseForQPack(courseTitle: String, qPackId: Long): LearnCourse

    suspend fun saveCourse(learnCourse: LearnCourse)

    fun getCourseViewIdRx(learnCourseId: Long): Single<Long>

    suspend fun getCourseViewId(learnCourseId: Long): Long?

    fun getCourseLastViewIdAsFlow(learnCourseId: Long): Flow<Long>

    suspend fun getCourseIdForViewId(viewId: Long): Long?

    suspend fun addCourseView(courseId: Long, viewId: Long)

    suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse

    suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Date?
    ): LearnCourse

    fun getAllCoursesRx(): Flowable<List<LearnCourse>>

    fun getAllCoursesAsFlow(): Flow<List<LearnCourse>>

    fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourse>>

    fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>>

    fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourse>>

    fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>>

    fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourse>>

    fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>>
}
