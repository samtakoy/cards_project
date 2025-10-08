package ru.samtakoy.features.learncourse.data.converters

import androidx.room.TypeConverter
import ru.samtakoy.features.learncourse.data.model.CourseTypeEntity

class CourseTypeConverter {

    @TypeConverter
    fun fromInt(id: Int): CourseTypeEntity = CourseTypeEntity.valueOfId(id)!!

    @TypeConverter
    fun fromCourseType(type: CourseTypeEntity): Int = type.dbId
}