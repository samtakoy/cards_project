package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.dao.LearnCourseViewDao
import ru.samtakoy.core.data.local.reps.CourseViewRepository
import ru.samtakoy.core.data.local.reps.CoursesRepository
import ru.samtakoy.core.data.local.reps.impl.CourseViewRepositoryImpl
import ru.samtakoy.core.data.local.reps.impl.CoursesRepositoryImpl
import ru.samtakoy.core.domain.CourseProgressUseCase
import ru.samtakoy.core.domain.CoursesPlanner
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.domain.impl.CourseProgressUseCaseImpl
import ru.samtakoy.core.domain.impl.CoursesPlannerImpl
import ru.samtakoy.core.domain.impl.NCoursesInteractorImpl
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapper
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapperImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapperImpl

@Module
internal interface CoursesModule {
    @Binds
    fun bindsCoursesRepository(impl: CoursesRepositoryImpl): CoursesRepository

    @Binds
    fun bindsCourseViewRepository(impl: CourseViewRepositoryImpl): CourseViewRepository

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

    companion object {
        @JvmStatic
        @Provides
        fun providesLearnCourseViewDao(db: MyRoomDb): LearnCourseViewDao {
            return db.courseViewDao()
        }
    }
}
