package ru.samtakoy.features.learncourse.data.mapper.schedule

import ru.samtakoy.features.learncourse.data.model.schedule.ScheduleTimeUnitEntity
import ru.samtakoy.features.learncourse.domain.model.schedule.ScheduleTimeUnit
import javax.inject.Inject

internal interface ScheduleTimeUnitMapper {
    fun mapToEntity(data: ScheduleTimeUnit): ScheduleTimeUnitEntity
    fun mapToDomain(data: ScheduleTimeUnitEntity): ScheduleTimeUnit
}

internal class ScheduleTimeUnitMapperImpl @Inject constructor(): ScheduleTimeUnitMapper {
    override fun mapToDomain(data: ScheduleTimeUnitEntity): ScheduleTimeUnit {
        return when (data) {
            ScheduleTimeUnitEntity.MINUTE -> ScheduleTimeUnit.MINUTE
            ScheduleTimeUnitEntity.HOUR -> ScheduleTimeUnit.HOUR
            ScheduleTimeUnitEntity.DAY -> ScheduleTimeUnit.DAY
            ScheduleTimeUnitEntity.WEEK -> ScheduleTimeUnit.WEEK
            ScheduleTimeUnitEntity.MONTH -> ScheduleTimeUnit.MONTH
            ScheduleTimeUnitEntity.YEAR -> ScheduleTimeUnit.YEAR
        }
    }

    override fun mapToEntity(data: ScheduleTimeUnit): ScheduleTimeUnitEntity {
        return when (data) {
            ScheduleTimeUnit.MINUTE -> ScheduleTimeUnitEntity.MINUTE
            ScheduleTimeUnit.HOUR -> ScheduleTimeUnitEntity.HOUR
            ScheduleTimeUnit.DAY -> ScheduleTimeUnitEntity.DAY
            ScheduleTimeUnit.WEEK -> ScheduleTimeUnitEntity.WEEK
            ScheduleTimeUnit.MONTH -> ScheduleTimeUnitEntity.MONTH
            ScheduleTimeUnit.YEAR -> ScheduleTimeUnitEntity.YEAR
        }
    }
}