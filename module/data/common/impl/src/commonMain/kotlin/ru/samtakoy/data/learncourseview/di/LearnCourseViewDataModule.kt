package ru.samtakoy.data.learncourseview.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.learncourse.CourseViewRepository
import ru.samtakoy.data.learncourseview.CourseViewRepositoryImpl
import ru.samtakoy.data.learncourseview.LearnCourseViewDao

internal fun learnCourseViewDataModule() = module {
    factoryOf(::CourseViewRepositoryImpl) bind CourseViewRepository::class
    single<LearnCourseViewDao> { get<MyRoomDb>().courseViewDao() }
}