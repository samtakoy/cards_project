package ru.samtakoy.domain.learncourse.di

import ru.samtakoy.domain.learncourse.CourseProgressUseCase
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.domain.learncourse.ViewHistoryProgressUseCase

interface LearnCourseDomainApiComponent {
    fun courseProgressUseCase(): CourseProgressUseCase
    fun coursesPlanner(): CoursesPlanner
    fun nCoursesInteractor(): NCoursesInteractor
    fun viewHistoryProgressUseCase(): ViewHistoryProgressUseCase
}