package ru.samtakoy.features.learncourse.data.mapper

import ru.samtakoy.features.learncourse.data.model.CourseTypeEntity
import ru.samtakoy.features.learncourse.domain.model.CourseType
import javax.inject.Inject

internal interface CourseTypeMapper {
    fun mapToDomain(data: CourseTypeEntity): CourseType
    fun mapToEntity(data: CourseType): CourseTypeEntity
}

internal class CourseTypeMapperImpl @Inject constructor() : CourseTypeMapper {
    override fun mapToDomain(data: CourseTypeEntity): CourseType {
        return when (data) {
            CourseTypeEntity.PRIMARY -> CourseType.PRIMARY
            CourseTypeEntity.SECONDARY -> CourseType.SECONDARY
            CourseTypeEntity.ADDITIONAL -> CourseType.ADDITIONAL
        }
    }

    override fun mapToEntity(data: CourseType): CourseTypeEntity {
        return when (data) {
            CourseType.PRIMARY -> CourseTypeEntity.PRIMARY
            CourseType.SECONDARY -> CourseTypeEntity.SECONDARY
            CourseType.ADDITIONAL -> CourseTypeEntity.ADDITIONAL
        }
    }
}