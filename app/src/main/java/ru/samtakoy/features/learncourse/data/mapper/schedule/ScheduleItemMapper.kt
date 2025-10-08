package ru.samtakoy.features.learncourse.data.mapper.schedule

import ru.samtakoy.features.learncourse.data.model.schedule.ScheduleItemEntity
import ru.samtakoy.features.learncourse.domain.model.schedule.ScheduleItem
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