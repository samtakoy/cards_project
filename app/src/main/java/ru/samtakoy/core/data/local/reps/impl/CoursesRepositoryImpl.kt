package ru.samtakoy.core.data.local.reps.impl

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.data.local.reps.CoursesRepository
import java.util.Arrays
import java.util.Date
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(
    private val db: MyRoomDb
) : CoursesRepository {

    override suspend fun getCourse(learnCourseId: Long): LearnCourseEntity? {
        return db.courseDao().getLearnCourse(learnCourseId)
    }

    override fun getCourseAsFlow(courseId: Long): Flow<LearnCourseEntity?> {
        return db.courseDao().getLearnCourseAsFlow(courseId)
    }

    override fun getCourseFlowableRx(learnCourseId: Long): Flowable<LearnCourseEntity> {
        return db.courseDao().getLearnCourseFlowableRx(learnCourseId)
    }

    override fun getCourseByMode(mode: LearnCourseMode): LearnCourseEntity {
        return db.courseDao().getLearnCourseByMode(mode.dbId)!!
    }

    override suspend fun updateCourse(course: LearnCourseEntity): Boolean {
        return db.courseDao().updateCourse(course) > 0
    }

    override suspend fun deleteCourse(courseId: Long) {
        db.courseDao().deleteCourseById(courseId)
    }

    override suspend fun deleteQPackCourses(qPackId: Long) {
        db.courseDao().deleteQPackCourses(qPackId)
    }

    override fun addNewCourseNow(newCourse: LearnCourseEntity): LearnCourseEntity {
        val id = db.courseDao().addLearnCourse(newCourse)
        return newCourse.copy(id = id)
    }

    override suspend fun addNewCourse(newCourse: LearnCourseEntity): LearnCourseEntity {
        val id = db.courseDao().addLearnCourse(newCourse)
        return newCourse.copy(id = id)
    }

    override fun getAllCoursesRx(): Flowable<List<LearnCourseEntity>> {
        return db.courseDao().getAllCoursesRx()
    }

    override fun getAllCoursesAsFlow(): Flow<List<LearnCourseEntity>> {
        return db.courseDao().getAllCoursesAsFlow()
    }

    override suspend fun getAllCourses(): List<LearnCourseEntity> {
        return db.courseDao().getAllCourses()
    }

    override fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourseEntity>> {
        return db.courseDao().getCoursesByIds(Arrays.asList(*targetCourseIds))
    }

    override fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourseEntity>> {
        return db.courseDao().getCoursesByIdsAsFlow(Arrays.asList(*targetCourseIds))
    }

    private fun modesToIds(targetModes: List<LearnCourseMode>): List<Int> {
        val result: MutableList<Int> = ArrayList(targetModes.size)
        for (mode in targetModes) {
            result.add(mode.dbId)
        }
        return result
    }

    override fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourseEntity>> {
        return db.courseDao().getCoursesByModes(modesToIds(targetModes))
    }

    override fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourseEntity>> {
        return db.courseDao().getCoursesByModesAsFlow(modesToIds(targetModes))
    }

    override fun getCoursesByModes(vararg mode: LearnCourseMode): Flowable<List<LearnCourseEntity>> {
        return db.courseDao().getLearnCourseByModes(Arrays.asList(*mode))
    }

    override fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourseEntity> {
        return db.courseDao().getLearnCourseByModesNow(Arrays.asList(*mode))
    }

    override fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourseEntity>> {
        return db.courseDao().getCoursesForQPackRx(qPackId)
    }

    override fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourseEntity>> {
        return db.courseDao().getCoursesForQPackAsFlow(qPackId)
    }

    // не в Rx стиле для сервиса, перенести в отдельный репозиторий?
    override fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourseEntity> {
        return db.courseDao().getOrderedCoursesLessThan(mode, repeatDate)
    }

    override fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourseEntity> {
        return db.courseDao().getOrderedCoursesMoreThan(mode, repeatDate)
    }

    companion object {
        private const val TAG = "CoursesRepositoryImpl"
    }
}
