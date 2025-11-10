package ru.samtakoy.data.cardtag.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.cardtag.CardTagDao
import ru.samtakoy.data.cardtag.TagDao
import ru.samtakoy.data.cardtag.TagsRepository
import ru.samtakoy.data.cardtag.TagsRepositoryImpl
import ru.samtakoy.data.cardtag.mapper.TagEntityMapper
import ru.samtakoy.data.cardtag.mapper.TagEntityMapperImpl
import ru.samtakoy.data.common.db.MyRoomDb

internal fun cardTagDataModule() = module {
    factoryOf(::TagEntityMapperImpl) bind TagEntityMapper::class
    factoryOf(::TagsRepositoryImpl) bind TagsRepository::class
    single<TagDao> { get<MyRoomDb>().tagDao() }
    single<CardTagDao> { get<MyRoomDb>().cardTagDao() }
}