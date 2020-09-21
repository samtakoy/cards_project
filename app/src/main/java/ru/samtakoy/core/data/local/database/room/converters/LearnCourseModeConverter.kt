package ru.samtakoy.core.data.local.database.room.converters

import androidx.room.TypeConverter
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode

class LearnCourseModeConverter {

    @TypeConverter
    fun fromLearnCourseMode(mode: LearnCourseMode): Int = mode.dbId

    @TypeConverter
    fun fromInd(id: Int): LearnCourseMode = LearnCourseMode.valueOfId(id)
}