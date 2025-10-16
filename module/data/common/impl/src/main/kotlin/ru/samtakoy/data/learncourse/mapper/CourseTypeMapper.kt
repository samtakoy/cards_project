package ru.samtakoy.data.learncourse.mapper

import ru.samtakoy.data.learncourse.model.CourseTypeEntity
import ru.samtakoy.domain.learncourse.CourseType

internal interface CourseTypeMapper {
    fun mapToDomain(data: CourseTypeEntity): CourseType
    fun mapToEntity(data: CourseType): CourseTypeEntity
}

internal class CourseTypeMapperImpl() : CourseTypeMapper {
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