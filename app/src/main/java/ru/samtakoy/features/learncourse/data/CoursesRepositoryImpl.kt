package ru.samtakoy.features.learncourse.data

import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.features.learncourse.data.mapper.LearnCourseMapper
import ru.samtakoy.features.learncourse.data.mapper.LearnCourseModeMapper
import ru.samtakoy.features.learncourse.data.model.LearnCourseModeEntity
import ru.samtakoy.features.learncourse.domain.model.CourseType
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import ru.samtakoy.features.learncourse.domain.model.LearnCourseMode
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import java.util.Arrays
import java.util.Date
import javax.inject.Inject

internal class CoursesRepositoryImpl @Inject constructor(
    private val courseDao: LearnCourseDao,
    private val courseMapper: LearnCourseMapper,
    private val modeMapper: LearnCourseModeMapper
) : CoursesRepository {

    override suspend fun getCourse(learnCourseId: Long): LearnCourse? {
        return courseDao.getLearnCourse(learnCourseId)?.let(courseMapper::mapToDomain)
    }

    override fun getCourseAsFlow(courseId: Long): Flow<LearnCourse?> {
        return courseDao.getLearnCourseAsFlow(courseId).map { it?.let(courseMapper::mapToDomain) }
    }

    override fun getCourseFlowableRx(learnCourseId: Long): Flowable<LearnCourse> {
        return courseDao.getLearnCourseFlowableRx(learnCourseId).map(courseMapper::mapToDomain)
    }

    override suspend fun updateCourse(course: LearnCourse): Boolean {
        return courseDao.updateCourse(courseMapper.mapToEntity(course)) > 0
    }

    override suspend fun deleteCourse(courseId: Long) {
        courseDao.deleteCourseById(courseId)
    }

    override suspend fun deleteQPackCourses(qPackId: Long) {
        courseDao.deleteQPackCourses(qPackId)
    }

    override fun addNewCourseSync(newCourse: LearnCourse): LearnCourse {
        val id = courseDao.addLearnCourse(courseMapper.mapToEntity(newCourse))
        return newCourse.copy(id = id)
    }

    override suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Date?
    ): LearnCourse {
        return addNewCourse(
            LearnCourse(
                id = 0L,
                qPackId = qPackId,
                courseType = courseType,
                primaryCourseId = 0L,
                title = title,
                mode = mode,
                repeatedCount = 0,
                cardIds = cardIds ?: emptyList(),
                restSchedule = restSchedule ?: Schedule(emptyList()),
                realizedSchedule = Schedule(emptyList()),
            repeatDate = repeatDate ?: DateUtils.currentTimeDate
            )
        )
    }

    override suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse {
        val id = courseDao.addLearnCourse(courseMapper.mapToEntity(newCourse))
        return newCourse.copy(id = id)
    }

    override fun getAllCoursesRx(): Flowable<List<LearnCourse>> {
        return courseDao.getAllCoursesRx().map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getAllCoursesAsFlow(): Flow<List<LearnCourse>> {
        return courseDao.getAllCoursesAsFlow().map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override suspend fun getAllCourses(): List<LearnCourse> {
        return courseDao.getAllCourses().map(courseMapper::mapToDomain)
    }

    override fun getCoursesByIds(targetCourseIds: Array<Long>): Flowable<List<LearnCourse>> {
        return courseDao.getCoursesByIds(Arrays.asList(*targetCourseIds)).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>> {
        return courseDao.getCoursesByIdsAsFlow(Arrays.asList(*targetCourseIds)).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    private fun modesToIds(targetModes: List<LearnCourseModeEntity>): List<Int> {
        val result: MutableList<Int> = ArrayList(targetModes.size)
        for (mode in targetModes) {
            result.add(mode.dbId)
        }
        return result
    }

    override fun getCoursesByModes(targetModes: List<LearnCourseMode>): Flowable<List<LearnCourse>> {
        return courseDao.getCoursesByModes(
            modes = modesToIds(targetModes.map(modeMapper::mapToEntity))
        ).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>> {
        return courseDao.getCoursesByModesAsFlow(
            modes = modesToIds(targetModes.map(modeMapper::mapToEntity))
        ).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getCoursesByModes(vararg mode: LearnCourseMode): Flowable<List<LearnCourse>> {
        return courseDao.getLearnCourseByModes(
            Arrays.asList(*mode).map(modeMapper::mapToEntity)
        ).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourse> {
        return courseDao.getLearnCourseByModesNow(
            Arrays.asList(*mode).map(modeMapper::mapToEntity)
        ).map(courseMapper::mapToDomain)
    }

    override fun getCoursesForQPackRx(qPackId: Long): Flowable<List<LearnCourse>> {
        return courseDao.getCoursesForQPackRx(qPackId).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>> {
        return courseDao.getCoursesForQPackAsFlow(qPackId).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    // не в Rx стиле для сервиса, перенести в отдельный репозиторий?
    override fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourse> {
        return courseDao.getOrderedCoursesLessThan(
            mode = modeMapper.mapToEntity(mode),
            repeatDate = repeatDate
        ).map(courseMapper::mapToDomain)
    }

    override fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Date): List<LearnCourse> {
        return courseDao.getOrderedCoursesMoreThan(
            mode = modeMapper.mapToEntity(mode),
            repeatDate = repeatDate
        ).map(courseMapper::mapToDomain)
    }
}
