package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapper
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapperImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapperImpl

@Module
internal interface CoursesModule {
    // View
    @Binds
    fun bindsCourseInfoViewStateMapper(impl: CourseInfoViewStateMapperImpl): CourseInfoViewStateMapper

    @Binds
    fun bindsCourseItemMapper(impl: CourseItemUiMapperImpl): CourseItemUiMapper


}
