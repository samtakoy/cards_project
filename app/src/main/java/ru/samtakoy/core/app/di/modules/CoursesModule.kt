package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.features.database.data.MyRoomDb
import ru.samtakoy.features.learncourseview.data.LearnCourseViewDao
import ru.samtakoy.features.learncourseview.data.CourseViewRepository
import ru.samtakoy.features.learncourse.data.CoursesRepository
import ru.samtakoy.features.learncourseview.data.CourseViewRepositoryImpl
import ru.samtakoy.features.learncourse.data.CoursesRepositoryImpl
import ru.samtakoy.features.learncourse.domain.CourseProgressUseCase
import ru.samtakoy.features.learncourse.domain.CoursesPlanner
import ru.samtakoy.features.learncourse.domain.NCoursesInteractor
import ru.samtakoy.features.learncourse.domain.CourseProgressUseCaseImpl
import ru.samtakoy.features.learncourse.domain.CoursesPlannerImpl
import ru.samtakoy.features.learncourse.domain.NCoursesInteractorImpl
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapper
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewStateMapperImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapper
import ru.samtakoy.core.presentation.courses.model.CourseItemUiMapperImpl
import ru.samtakoy.features.learncourse.data.LearnCourseDao
import ru.samtakoy.features.learncourse.data.mapper.CourseTypeMapper
import ru.samtakoy.features.learncourse.data.mapper.CourseTypeMapperImpl
import ru.samtakoy.features.learncourse.data.mapper.LearnCourseMapper
import ru.samtakoy.features.learncourse.data.mapper.LearnCourseMapperImpl
import ru.samtakoy.features.learncourse.data.mapper.LearnCourseModeMapper
import ru.samtakoy.features.learncourse.data.mapper.LearnCourseModeMapperImpl
import ru.samtakoy.features.learncourse.data.mapper.schedule.ScheduleItemMapper
import ru.samtakoy.features.learncourse.data.mapper.schedule.ScheduleItemMapperImpl
import ru.samtakoy.features.learncourse.data.mapper.schedule.ScheduleMapper
import ru.samtakoy.features.learncourse.data.mapper.schedule.ScheduleMapperImpl
import ru.samtakoy.features.learncourse.data.mapper.schedule.ScheduleTimeUnitMapper
import ru.samtakoy.features.learncourse.data.mapper.schedule.ScheduleTimeUnitMapperImpl

@Module
internal interface CoursesModule {
    @Binds
    fun bindsScheduleItemMapper(impl: ScheduleItemMapperImpl): ScheduleItemMapper

    @Binds
    fun bindsScheduleMapper(impl: ScheduleMapperImpl): ScheduleMapper

    @Binds
    fun bindsScheduleTimeUnitMapper(impl: ScheduleTimeUnitMapperImpl): ScheduleTimeUnitMapper

    @Binds
    fun bindsLearnCourseModeMapper(impl: LearnCourseModeMapperImpl): LearnCourseModeMapper

    @Binds
    fun bindsCourseTypeMapper(impl: CourseTypeMapperImpl): CourseTypeMapper

    @Binds
    fun bindsLearnCourseMapper(impl: LearnCourseMapperImpl): LearnCourseMapper

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

        @JvmStatic
        @Provides
        fun providesLearnCourseDao(db: MyRoomDb): LearnCourseDao {
            return db.courseDao()
        }
    }
}
