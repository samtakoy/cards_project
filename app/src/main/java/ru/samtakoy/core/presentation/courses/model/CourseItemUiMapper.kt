package ru.samtakoy.core.presentation.courses.model

import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity
import ru.samtakoy.core.data.local.database.room.entities.getDynamicTitle
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import javax.inject.Inject

interface CourseItemUiMapper {
    fun map(item: LearnCourseEntity): CourseItemUiModel
    fun map(items: List<LearnCourseEntity>): List<CourseItemUiModel>
}

internal class CourseItemUiMapperImpl @Inject constructor(): CourseItemUiMapper {
    override fun map(item: LearnCourseEntity): CourseItemUiModel {
        return CourseItemUiModel(
            id = LongUiId(item.id),
            title = item.getDynamicTitle().asAnnotated()
        )
    }

    override fun map(items: List<LearnCourseEntity>): List<CourseItemUiModel> {
        return items.map(::map)
    }
}