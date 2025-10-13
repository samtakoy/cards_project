package ru.samtakoy.data.learncourse.mapper.schedule

import ru.samtakoy.data.learncourse.model.schedule.ScheduleItemEntity
import ru.samtakoy.domain.learncourse.schedule.ScheduleItem
import javax.inject.Inject

internal interface ScheduleItemMapper {
    fun mapToEntity(data: ScheduleItem): ScheduleItemEntity
    fun mapToDomain(data: ScheduleItemEntity): ScheduleItem
}

internal class ScheduleItemMapperImpl @Inject constructor(
    private val timeUnitMapper: ScheduleTimeUnitMapper
) : ScheduleItemMapper {
    override fun mapToEntity(data: ScheduleItem): ScheduleItemEntity {
        return ScheduleItemEntity(
            dimension = data.dimension,
            timeUnit = timeUnitMapper.mapToEntity(data.timeUnit)
        )
    }

    override fun mapToDomain(data: ScheduleItemEntity): ScheduleItem {
        return ScheduleItem(
            dimension = data.dimension,
            timeUnit = timeUnitMapper.mapToDomain(data.timeUnit)
        )
    }
}