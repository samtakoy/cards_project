package ru.samtakoy.data.theme.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.di.DataScope
import ru.samtakoy.data.theme.ThemeDao
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.data.theme.ThemesRepositoryImpl
import ru.samtakoy.data.theme.mapper.ThemeEntityMapper
import ru.samtakoy.data.theme.mapper.ThemeEntityMapperImpl
import javax.inject.Singleton

@Module
internal interface ThemeDataModule {
    @Binds
    fun bindThemeEntityMapper(impl: ThemeEntityMapperImpl): ThemeEntityMapper

    @Binds @DataScope
    fun bindsThemesRepository(impl: ThemesRepositoryImpl): ThemesRepository

    companion object {
        @JvmStatic
        @Provides
        fun providesThemeDao(db: MyRoomDb): ThemeDao {
            return db.themeDao()
        }
    }
}