package ru.samtakoy.data.learncourse.converters

import androidx.room.TypeConverter
import ru.samtakoy.data.learncourse.model.LearnCourseModeEntity

internal class LearnCourseModeConverter {

    @TypeConverter
    fun fromLearnCourseMode(mode: LearnCourseModeEntity): Int = mode.dbId

    @TypeConverter
    fun fromInd(id: Int): LearnCourseModeEntity = LearnCourseModeEntity.Companion.valueOfId(id)!!
}