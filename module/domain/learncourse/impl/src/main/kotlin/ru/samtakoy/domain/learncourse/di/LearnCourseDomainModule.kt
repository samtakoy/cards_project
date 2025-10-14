package ru.samtakoy.domain.learncourse.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.learncourse.CourseProgressUseCase
import ru.samtakoy.domain.learncourse.CourseProgressUseCaseImpl
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.domain.learncourse.CoursesPlannerImpl
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.domain.learncourse.NCoursesInteractorImpl
import ru.samtakoy.domain.learncourse.ViewHistoryProgressUseCase
import ru.samtakoy.domain.learncourse.ViewHistoryProgressUseCaseImpl

@Module
internal interface LearnCourseDomainModule {
    @Binds
    fun bindsCoursesInteractor(impl: NCoursesInteractorImpl): NCoursesInteractor

    @Binds
    fun bindsCourseProgressUseCase(impl: CourseProgressUseCaseImpl): CourseProgressUseCase

    @Binds
    fun bindsCoursesPlanner(impl: CoursesPlannerImpl): CoursesPlanner

    @Binds
    fun bindsViewHistoryProgressUseCase(impl: ViewHistoryProgressUseCaseImpl): ViewHistoryProgressUseCase
}