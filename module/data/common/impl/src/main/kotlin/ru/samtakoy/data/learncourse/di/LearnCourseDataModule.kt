package ru.samtakoy.data.learncourse.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.learncourse.CoursesRepository
import ru.samtakoy.data.learncourse.CoursesRepositoryImpl
import ru.samtakoy.data.learncourse.LearnCourseDao
import ru.samtakoy.data.learncourse.mapper.CourseTypeMapper
import ru.samtakoy.data.learncourse.mapper.CourseTypeMapperImpl
import ru.samtakoy.data.learncourse.mapper.LearnCourseMapper
import ru.samtakoy.data.learncourse.mapper.LearnCourseMapperImpl
import ru.samtakoy.data.learncourse.mapper.LearnCourseModeMapper
import ru.samtakoy.data.learncourse.mapper.LearnCourseModeMapperImpl
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleItemMapper
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleItemMapperImpl
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleMapper
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleMapperImpl
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleTimeUnitMapper
import ru.samtakoy.data.learncourse.mapper.schedule.ScheduleTimeUnitMapperImpl

@Module
internal interface LearnCourseDataModule {

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


    companion object {

        @JvmStatic
        @Provides
        fun providesLearnCourseDao(db: MyRoomDb): LearnCourseDao {
            return db.courseDao()
        }
    }
}