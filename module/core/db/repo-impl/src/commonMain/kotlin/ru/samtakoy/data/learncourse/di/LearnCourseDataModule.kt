package ru.samtakoy.data.learncourse.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.domain.learncourse.CoursesRepository
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

internal fun learnCourseDataModule() = module {
    factoryOf(::ScheduleItemMapperImpl) bind ScheduleItemMapper::class
    factoryOf(::ScheduleMapperImpl) bind ScheduleMapper::class
    factoryOf(::ScheduleTimeUnitMapperImpl) bind ScheduleTimeUnitMapper::class
    factoryOf(::LearnCourseModeMapperImpl) bind LearnCourseModeMapper::class
    factoryOf(::CourseTypeMapperImpl) bind CourseTypeMapper::class
    factoryOf(::LearnCourseMapperImpl) bind LearnCourseMapper::class
    singleOf(::CoursesRepositoryImpl) bind CoursesRepository::class
    single<LearnCourseDao> { get<MyRoomDb>().courseDao() }
}