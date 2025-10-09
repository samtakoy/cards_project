package ru.samtakoy.features.learncourse.data.converters

import androidx.room.TypeConverter
import ru.samtakoy.features.learncourse.data.model.LearnCourseModeEntity

class LearnCourseModeConverter {

    @TypeConverter
    fun fromLearnCourseMode(mode: LearnCourseModeEntity): Int = mode.dbId

    @TypeConverter
    fun fromInd(id: Int): LearnCourseModeEntity = LearnCourseModeEntity.valueOfId(id)!!
}