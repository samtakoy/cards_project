package ru.samtakoy.data.learncourse.mapper

import ru.samtakoy.data.learncourse.model.LearnCourseModeEntity
import ru.samtakoy.domain.learncourse.LearnCourseMode

internal interface LearnCourseModeMapper {
    fun mapToDomain(data: LearnCourseModeEntity): LearnCourseMode
    fun mapToEntity(data: LearnCourseMode): LearnCourseModeEntity
}

internal class LearnCourseModeMapperImpl() : LearnCourseModeMapper {
    override fun mapToDomain(data: LearnCourseModeEntity): LearnCourseMode {
        return when (data) {
            LearnCourseModeEntity.PREPARING -> LearnCourseMode.PREPARING
            LearnCourseModeEntity.LEARN_WAITING -> LearnCourseMode.LEARN_WAITING
            LearnCourseModeEntity.LEARNING -> LearnCourseMode.LEARNING
            LearnCourseModeEntity.REPEAT_WAITING -> LearnCourseMode.REPEAT_WAITING
            LearnCourseModeEntity.REPEATING -> LearnCourseMode.REPEATING
            LearnCourseModeEntity.COMPLETED -> LearnCourseMode.COMPLETED
        }
    }

    override fun mapToEntity(data: LearnCourseMode): LearnCourseModeEntity {
        return when (data) {
            LearnCourseMode.PREPARING -> LearnCourseModeEntity.PREPARING
            LearnCourseMode.LEARN_WAITING -> LearnCourseModeEntity.LEARN_WAITING
            LearnCourseMode.LEARNING -> LearnCourseModeEntity.LEARNING
            LearnCourseMode.REPEAT_WAITING -> LearnCourseModeEntity.REPEAT_WAITING
            LearnCourseMode.REPEATING -> LearnCourseModeEntity.REPEATING
            LearnCourseMode.COMPLETED -> LearnCourseModeEntity.COMPLETED
        }
    }
}