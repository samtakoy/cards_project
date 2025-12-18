package ru.samtakoy.data.learncourse.mapper.schedule

import ru.samtakoy.data.learncourse.model.schedule.ScheduleEntity
import ru.samtakoy.domain.learncourse.schedule.Schedule

internal interface ScheduleMapper {
    fun mapToEntity(data: Schedule): ScheduleEntity
    fun mapToDomain(data: ScheduleEntity): Schedule
}

internal class ScheduleMapperImpl(
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