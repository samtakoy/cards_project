package ru.samtakoy.core.presentation.courses.model

import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.features.learncourse.domain.utils.getDynamicTitle
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId
import ru.samtakoy.features.learncourse.domain.model.LearnCourse
import javax.inject.Inject

interface CourseItemUiMapper {
    fun map(item: LearnCourse): CourseItemUiModel
    fun map(items: List<LearnCourse>): List<CourseItemUiModel>
}

internal class CourseItemUiMapperImpl @Inject constructor(): CourseItemUiMapper {
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