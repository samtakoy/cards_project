package ru.samtakoy.core.data.local.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType

class CourseTypeConverter {

    @TypeConverter
    fun fromInt(id: Int): CourseType = CourseType.valueOfId(id)

    @TypeConverter
    fun fromCourseType(type: CourseType): Int = type.dbId
}