package ru.samtakoy.core.presentation.courses.model

import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.domain.learncourse.LearnCourse
import ru.samtakoy.domain.learncourse.getDynamicTitle
import ru.samtakoy.presentation.utils.asAnnotated

interface CourseItemUiMapper {
    fun map(item: LearnCourse): CourseItemUiModel
    fun map(items: List<LearnCourse>): List<CourseItemUiModel>
}

internal class CourseItemUiMapperImpl(): CourseItemUiMapper {
    override fun map(item: LearnCourse): CourseItemUiModel {
        return CourseItemUiModel(
            id = LongUiId(item.id),
            title = item.getDynamicTitle().asAnnotated()
        )
    }

    override fun map(items: List<LearnCourse>): List<CourseItemUiModel> {
        return items.map(::map)
    }
}