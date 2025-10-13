package ru.samtakoy.data.card.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.card.CardDao
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.data.card.CardsRepositoryImpl
import ru.samtakoy.data.card.mapper.CardEntityMapper
import ru.samtakoy.data.card.mapper.CardEntityMapperImpl
import ru.samtakoy.data.card.mapper.CardWithTagsEntityMapper
import ru.samtakoy.data.card.mapper.CardWithTagsEntityMapperImpl
import ru.samtakoy.data.cardtag.TagsRepository
import ru.samtakoy.data.cardtag.TagsRepositoryImpl
import ru.samtakoy.data.cardtag.mapper.TagEntityMapper
import ru.samtakoy.data.cardtag.mapper.TagEntityMapperImpl
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.di.DataScope

@Module
internal interface CardDataModule {
    @Binds
    fun bindCardEntityMapper(impl: CardEntityMapperImpl): CardEntityMapper

    @Binds
    fun bindCardWithTagsEntityMapper(impl: CardWithTagsEntityMapperImpl): CardWithTagsEntityMapper

    @Binds @DataScope
    fun bindsCardsRepository(impl: CardsRepositoryImpl): CardsRepository

    companion object {
        @JvmStatic
        @Provides
        fun providesCardDao(db: MyRoomDb): CardDao {
            return db.cardDao()
        }
    }

}