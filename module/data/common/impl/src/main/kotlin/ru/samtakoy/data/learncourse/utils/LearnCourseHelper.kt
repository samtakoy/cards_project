package ru.samtakoy.data.learncourse.utils

import ru.samtakoy.domain.learncourse.LearnCourse

object LearnCourseHelper {
    fun getLearnCourseIds(list: List<LearnCourse>): Array<Long> {
        return list.map { it.id }.toTypedArray()
    }
}