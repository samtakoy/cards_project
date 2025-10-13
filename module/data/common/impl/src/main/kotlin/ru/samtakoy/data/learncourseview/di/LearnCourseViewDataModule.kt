package ru.samtakoy.data.learncourseview.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.learncourse.CourseViewRepository
import ru.samtakoy.data.learncourseview.CourseViewRepositoryImpl
import ru.samtakoy.data.learncourseview.LearnCourseViewDao

@Module
internal interface LearnCourseViewDataModule {

    @Binds
    fun bindsCourseViewRepository(impl: CourseViewRepositoryImpl): CourseViewRepository

    companion object {
        @JvmStatic
        @Provides
        fun providesLearnCourseViewDao(db: MyRoomDb): LearnCourseViewDao {
            return db.courseViewDao()
        }
    }
}