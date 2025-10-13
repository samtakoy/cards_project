package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapper
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapperImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapperImpl
import ru.samtakoy.domain.learncourse.CourseProgressUseCase
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.features.learncourse.domain.CourseProgressUseCaseImpl
import ru.samtakoy.features.learncourse.domain.CoursesPlannerImpl
import ru.samtakoy.features.learncourse.domain.NCoursesInteractorImpl

@Module
internal interface CoursesModule {
    @Binds
    fun bindsCoursesInteractor(impl: NCoursesInteractorImpl): NCoursesInteractor

    @Binds
    fun bindsCourseProgressUseCase(impl: CourseProgressUseCaseImpl): CourseProgressUseCase

    @Binds
    fun bindsCoursesPlanner(impl: CoursesPlannerImpl): CoursesPlanner

    // View
    @Binds
    fun bindsCourseInfoViewStateMapper(impl: CourseInfoViewStateMapperImpl): CourseInfoViewStateMapper

    @Binds
    fun bindsCourseItemMapper(impl: CourseItemUiMapperImpl): CourseItemUiMapper


}
