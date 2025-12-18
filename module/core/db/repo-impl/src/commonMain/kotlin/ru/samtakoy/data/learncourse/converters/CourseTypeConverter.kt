package ru.samtakoy.data.learncourse.converters

import androidx.room.TypeConverter
import ru.samtakoy.data.learncourse.model.CourseTypeEntity

internal class CourseTypeConverter {

    @TypeConverter
    fun fromInt(id: Int): CourseTypeEntity = CourseTypeEntity.Companion.valueOfId(id)!!

    @TypeConverter
    fun fromCourseType(type: CourseTypeEntity): Int = type.dbId
}