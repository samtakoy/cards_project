package ru.samtakoy.data.learncourse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.learncourse.mapper.LearnCourseMapper
import ru.samtakoy.data.learncourse.mapper.LearnCourseModeMapper
import ru.samtakoy.data.learncourse.model.LearnCourseModeEntity
import ru.samtakoy.domain.learncourse.CourseType
import ru.samtakoy.domain.learncourse.LearnCourse
import ru.samtakoy.domain.learncourse.LearnCourseMode
import ru.samtakoy.domain.learncourse.schedule.Schedule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class CoursesRepositoryImpl(
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

    override suspend fun updateCourse(course: LearnCourse): Boolean {
        return courseDao.updateCourse(courseMapper.mapToEntity(course)) > 0
    }

    override suspend fun deleteCourse(courseId: Long) {
        courseDao.deleteCourseById(courseId)
    }

    override suspend fun deleteQPackCourses(qPackId: Long) {
        courseDao.deleteQPackCourses(qPackId)
    }

    @OptIn(ExperimentalTime::class)
    override fun addNewCourseSync(newCourse: LearnCourse): LearnCourse {
        val id = courseDao.addLearnCourse(courseMapper.mapToEntity(newCourse))
        return newCourse.copy(id = id)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun addNewCourse(
        qPackId: Long,
        courseType: CourseType,
        title: String,
        mode: LearnCourseMode,
        cardIds: List<Long>?,
        restSchedule: Schedule?,
        repeatDate: Instant?
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

    @OptIn(ExperimentalTime::class)
    override suspend fun addNewCourse(newCourse: LearnCourse): LearnCourse {
        val id = courseDao.addLearnCourse(courseMapper.mapToEntity(newCourse))
        return newCourse.copy(id = id)
    }

    override fun getAllCoursesAsFlow(): Flow<List<LearnCourse>> {
        return courseDao.getAllCoursesAsFlow().map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override suspend fun getAllCourses(): List<LearnCourse> {
        return courseDao.getAllCourses().map(courseMapper::mapToDomain)
    }

    override fun getCoursesByIdsAsFlow(targetCourseIds: Array<Long>): Flow<List<LearnCourse>> {
        return courseDao.getCoursesByIdsAsFlow(targetCourseIds.toList()).map { list ->
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

    override fun getCoursesByModesAsFlow(targetModes: List<LearnCourseMode>): Flow<List<LearnCourse>> {
        return courseDao.getCoursesByModesAsFlow(
            modes = modesToIds(targetModes.map(modeMapper::mapToEntity))
        ).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    override fun getCoursesByModesNow(vararg mode: LearnCourseMode): List<LearnCourse> {
        return courseDao.getLearnCourseByModesNow(
            mode.toList().map(modeMapper::mapToEntity)
        ).map(courseMapper::mapToDomain)
    }

    override fun getCoursesForQPackAsFlow(qPackId: Long): Flow<List<LearnCourse>> {
        return courseDao.getCoursesForQPackAsFlow(qPackId).map { list ->
            list.map(courseMapper::mapToDomain)
        }
    }

    // не в Rx стиле для сервиса, перенести в отдельный репозиторий?
    @OptIn(ExperimentalTime::class)
    override fun getOrderedCoursesLessThan(mode: LearnCourseMode, repeatDate: Instant): List<LearnCourse> {
        return courseDao.getOrderedCoursesLessThan(
            mode = modeMapper.mapToEntity(mode),
            repeatDate = DateUtils.dateToDbSerialized(repeatDate)
        ).map(courseMapper::mapToDomain)
    }

    @OptIn(ExperimentalTime::class)
    override fun getOrderedCoursesMoreThan(mode: LearnCourseMode, repeatDate: Instant): List<LearnCourse> {
        return courseDao.getOrderedCoursesMoreThan(
            mode = modeMapper.mapToEntity(mode),
            repeatDate = DateUtils.dateToDbSerialized(repeatDate)
        ).map(courseMapper::mapToDomain)
    }
}
