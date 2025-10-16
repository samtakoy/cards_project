package ru.samtakoy.domain.learncourse.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.learncourse.CourseProgressUseCase
import ru.samtakoy.domain.learncourse.CourseProgressUseCaseImpl
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.domain.learncourse.CoursesPlannerImpl
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.domain.learncourse.NCoursesInteractorImpl
import ru.samtakoy.domain.learncourse.ViewHistoryProgressUseCase
import ru.samtakoy.domain.learncourse.ViewHistoryProgressUseCaseImpl

fun learnCourseDomainModule() = module {
    factoryOf(::NCoursesInteractorImpl) bind NCoursesInteractor::class
    factoryOf(::CourseProgressUseCaseImpl) bind CourseProgressUseCase::class
    factoryOf(::CoursesPlannerImpl) bind CoursesPlanner::class
    factoryOf(::ViewHistoryProgressUseCaseImpl) bind ViewHistoryProgressUseCase::class
}