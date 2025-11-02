package ru.samtakoy.core.presentation.courses.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModelImpl
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapper
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapperImpl
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModelImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapperImpl
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModelImpl
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.themes.entry.CourseListEntryImpl

fun coursesPresentationModule() = module {
    factory<MainTabFeatureEntry>(named<CourseListRoute>()) { CourseListEntryImpl(get()) }

    factoryOf(::CourseItemUiMapperImpl) bind CourseItemUiMapper::class

    factoryOf(::CourseInfoViewStateMapperImpl) bind CourseInfoViewStateMapper::class
    viewModelOf(::CourseInfoViewModelImpl)

    viewModelOf(::CoursesListViewModelImpl)

    viewModelOf(::SelectCourseViewModelImpl)
}
