package ru.samtakoy.data.view.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.view.ViewHistoryRepository
import ru.samtakoy.data.view.ViewHistoryRepositoryImpl
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapper
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapperEx
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapperExImpl
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapperImpl
import ru.samtakoy.data.view.model.ViewHistoryDao

internal fun viewDataModule() = module {
    factoryOf(::ViewHistoryEntityMapperImpl) bind ViewHistoryEntityMapper::class
    factoryOf(::ViewHistoryEntityMapperExImpl) bind ViewHistoryEntityMapperEx::class
    singleOf(::ViewHistoryRepositoryImpl) bind ViewHistoryRepository::class
    single<ViewHistoryDao> { get<MyRoomDb>().viewHistoryDao() }
}