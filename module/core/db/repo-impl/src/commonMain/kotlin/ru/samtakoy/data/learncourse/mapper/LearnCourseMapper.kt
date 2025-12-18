package ru.samtakoy.data.learncourse.mapper

import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleMapper
import ru.samtakoy.data.learncourse.model.LearnCourseEntity
import ru.samtakoy.domain.learncourse.LearnCourse
import kotlin.time.ExperimentalTime

internal interface LearnCourseMapper {
    fun mapToDomain(data: LearnCourseEntity): LearnCourse
    fun mapToEntity(data: LearnCourse): LearnCourseEntity
}

internal class LearnCourseMapperImpl(
    private val courseTypeMapper: CourseTypeMapper,
    private val courseModeMapper: LearnCourseModeMapper,
    private val scheduleMapper: ScheduleMapper
) : LearnCourseMapper {

    override fun mapToDomain(data: LearnCourseEntity): LearnCourse {
        @OptIn(ExperimentalTime::class)
        return LearnCourse(
            id = data.id,
            qPackId = data.qPackId,
            courseType = courseTypeMapper.mapToDomain(data.courseType),
            primaryCourseId = data.primaryCourseId,
            title = data.title,
            mode = courseModeMapper.mapToDomain(data.mode),
            repeatedCount = data.repeatedCount,
            cardIds = data.cardIds,
            restSchedule = scheduleMapper.mapToDomain(data.restSchedule),
            realizedSchedule = scheduleMapper.mapToDomain(data.realizedSchedule),
            repeatDate = DateUtils.dateFromDbSerialized(data.repeatDate)
        )
    }

    override fun mapToEntity(data: LearnCourse): LearnCourseEntity {
        @OptIn(ExperimentalTime::class)
        return LearnCourseEntity(
            id = data.id,
            qPackId = data.qPackId,
            courseType = courseTypeMapper.mapToEntity(data.courseType),
            primaryCourseId = data.primaryCourseId,
            title = data.title,
            mode = courseModeMapper.mapToEntity(data.mode),
            repeatedCount = data.repeatedCount,
            cardIds = data.cardIds,
            restSchedule = scheduleMapper.mapToEntity(data.restSchedule),
            realizedSchedule = scheduleMapper.mapToEntity(data.realizedSchedule),
            repeatDate = DateUtils.dateToDbSerialized(data.repeatDate)
        )
    }
}