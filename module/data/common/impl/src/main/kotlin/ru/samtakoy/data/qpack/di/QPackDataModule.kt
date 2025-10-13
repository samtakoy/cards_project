package ru.samtakoy.data.qpack.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.data.common.db.MyRoomDb
import ru.samtakoy.data.di.DataScope
import ru.samtakoy.data.qpack.QPackDao
import ru.samtakoy.data.qpack.QPacksRepository
import ru.samtakoy.data.qpack.QPacksRepositoryImpl
import ru.samtakoy.data.qpack.mapper.QPackEntityMapper
import ru.samtakoy.data.qpack.mapper.QPackEntityMapperImpl

@Module
internal interface QPackDataModule {
    @Binds
    fun bindsQPackEntityMapper(impl: QPackEntityMapperImpl): QPackEntityMapper

    @Binds @DataScope
    fun bindsQPacksRepository(impl: QPacksRepositoryImpl): QPacksRepository

    companion object {
        @JvmStatic
        @Provides
        fun providesQPackDao(db: MyRoomDb): QPackDao {
            return db.qPackDao()
        }
    }
}