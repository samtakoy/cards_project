package ru.samtakoy.features.learncourse.data.utils

import ru.samtakoy.features.learncourse.domain.model.LearnCourse

object LearnCourseHelper {
    fun getLearnCourseIds(list: List<LearnCourse>): Array<Long> {
        return list.map { it.id }.toTypedArray()
    }
}