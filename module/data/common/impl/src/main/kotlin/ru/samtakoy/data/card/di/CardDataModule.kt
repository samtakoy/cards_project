package ru.samtakoy.data.card.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.card.CardDao
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.card.CardsRepositoryImpl
import ru.samtakoy.data.card.mapper.CardEntityMapper
import ru.samtakoy.data.card.mapper.CardEntityMapperImpl
import ru.samtakoy.data.card.mapper.CardWithTagsEntityMapper
import ru.samtakoy.data.card.mapper.CardWithTagsEntityMapperImpl
import ru.samtakoy.data.common.db.MyRoomDb

internal fun cardDataModule() = module {
    factoryOf(::CardEntityMapperImpl) bind CardEntityMapper::class
    factoryOf(::CardWithTagsEntityMapperImpl) bind CardWithTagsEntityMapper::class
    singleOf(::CardsRepositoryImpl) bind CardsRepository::class
    single<CardDao> { get<MyRoomDb>().cardDao() }
}