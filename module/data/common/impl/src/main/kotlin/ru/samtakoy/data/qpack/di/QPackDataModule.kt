package ru.samtakoy.data.qpack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.qpack.QPackDao
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.qpack.QPacksRepositoryImpl
import ru.samtakoy.data.qpack.mapper.QPackEntityMapper
import ru.samtakoy.data.qpack.mapper.QPackEntityMapperImpl

internal fun qPackDataModule() = module {
    factoryOf(::QPackEntityMapperImpl) bind QPackEntityMapper::class
    singleOf(::QPacksRepositoryImpl) bind QPacksRepository::class
    single<QPackDao> { get<MyRoomDb>().qPackDao() }
}