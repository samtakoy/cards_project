package ru.samtakoy.data.theme.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.theme.ThemeDao
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.data.theme.ThemesRepositoryImpl
import ru.samtakoy.data.theme.mapper.ThemeEntityMapper
import ru.samtakoy.data.theme.mapper.ThemeEntityMapperImpl

internal fun themeDataModule() = module {
    factoryOf(::ThemeEntityMapperImpl) bind ThemeEntityMapper::class
    singleOf(::ThemesRepositoryImpl) bind ThemesRepository::class
    single<ThemeDao> { get<MyRoomDb>().themeDao() }
}