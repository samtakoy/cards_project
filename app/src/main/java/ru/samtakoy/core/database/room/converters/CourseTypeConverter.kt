package ru.samtakoy.core.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.database.room.entities.types.CourseType

class CourseTypeConverter {

    @TypeConverter
    fun fromInt(id: Int): CourseType = CourseType.valueOfId(id)

    @TypeConverter
    fun fromCourseType(type: CourseType): Int = type.dbId
}