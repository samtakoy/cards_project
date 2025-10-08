package ru.samtakoy.features.learncourse.data.mapper

import ru.samtakoy.features.learncourse.data.model.LearnCourseModeEntity
import ru.samtakoy.features.learncourse.domain.model.LearnCourseMode
import javax.inject.Inject

internal interface LearnCourseModeMapper {
    fun mapToDomain(data: LearnCourseModeEntity): LearnCourseMode
    fun mapToEntity(data: LearnCourseMode): LearnCourseModeEntity
}

internal class LearnCourseModeMapperImpl @Inject constructor() : LearnCourseModeMapper {
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