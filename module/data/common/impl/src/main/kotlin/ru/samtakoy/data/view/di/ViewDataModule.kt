package ru.samtakoy.data.view.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.view.ViewHistoryRepository
import ru.samtakoy.data.view.ViewHistoryRepositoryImpl
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapper
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapperEx
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapperExImpl
import ru.samtakoy.data.view.mapper.ViewHistoryEntityMapperImpl
import ru.samtakoy.data.view.model.ViewHistoryDao

@Module
internal interface ViewDataModule {
    @Binds
    fun bindsViewHistoryEntityMapper(impl: ViewHistoryEntityMapperImpl): ViewHistoryEntityMapper

    @Binds
    fun bindsViewHistoryEntityMapperEx(impl: ViewHistoryEntityMapperExImpl): ViewHistoryEntityMapperEx

    @Binds
    fun bindsViewHistoryRepository(impl: ViewHistoryRepositoryImpl): ViewHistoryRepository

    companion object {
        @Provides
        @JvmStatic
        fun providesViewHistoryDao(db: MyRoomDb): ViewHistoryDao {
            return db.viewHistoryDao()
        }
    }
}