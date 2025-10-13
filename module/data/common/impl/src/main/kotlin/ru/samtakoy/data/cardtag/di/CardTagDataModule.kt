package ru.samtakoy.data.cardtag.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.cardtag.CardTagDao
import ru.samtakoy.data.cardtag.TagDao
import ru.samtakoy.data.cardtag.TagsRepository
import ru.samtakoy.data.cardtag.TagsRepositoryImpl
import ru.samtakoy.data.cardtag.mapper.TagEntityMapper
import ru.samtakoy.data.cardtag.mapper.TagEntityMapperImpl
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.di.DataScope

@Module
internal interface CardTagDataModule {

    @Binds
    fun bindsTagEntityMapper(impl: TagEntityMapperImpl): TagEntityMapper

    @Binds @DataScope
    fun bindsTagsRepository(impl: TagsRepositoryImpl): TagsRepository

    companion object {
        @JvmStatic
        @Provides
        fun providesTagDao(db: MyRoomDb): TagDao {
            return db.tagDao()
        }

        @JvmStatic
        @Provides
        fun providesCardTagDao(db: MyRoomDb): CardTagDao {
            return db.cardTagDao()
        }
    }
}