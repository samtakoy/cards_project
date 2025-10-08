package ru.samtakoy.features.learncourse.data.mapper.schedule

import ru.samtakoy.features.learncourse.data.model.schedule.ScheduleEntity
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import javax.inject.Inject

internal interface ScheduleMapper {
    fun mapToEntity(data: Schedule): ScheduleEntity
    fun mapToDomain(data: ScheduleEntity): Schedule
}

internal class ScheduleMapperImpl @Inject constructor(
    private val scheduleItemMapper: ScheduleItemMapper
) : ScheduleMapper {
    override fun mapToEntity(data: Schedule): ScheduleEntity {
        return ScheduleEntity(
            items = data.items.map(scheduleItemMapper::mapToEntity)
        )
    }

    override fun mapToDomain(data: ScheduleEntity): Schedule {
        return Schedule(
            items = data.items.map(scheduleItemMapper::mapToDomain)
        )
    }
}