package ru.samtakoy.features.views.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.features.database.data.MyRoomDb
import ru.samtakoy.features.views.data.local.ViewHistoryRepository
import ru.samtakoy.features.views.data.local.ViewHistoryRepositoryImpl
import ru.samtakoy.features.views.data.local.mapper.ViewHistoryEntityMapper
import ru.samtakoy.features.views.data.local.mapper.ViewHistoryEntityMapperEx
import ru.samtakoy.features.views.data.local.mapper.ViewHistoryEntityMapperExImpl
import ru.samtakoy.features.views.data.local.mapper.ViewHistoryEntityMapperImpl
import ru.samtakoy.features.views.data.local.model.ViewHistoryDao
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import ru.samtakoy.features.views.domain.ViewHistoryInteractorImpl
import ru.samtakoy.features.views.domain.ViewHistoryProgressUseCase
import ru.samtakoy.features.views.domain.ViewHistoryProgressUseCaseImpl
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapperImpl

@Module
internal interface ViewsFeatureModule {

    // data

    @Binds
    fun bindsViewHistoryEntityMapper(impl: ViewHistoryEntityMapperImpl): ViewHistoryEntityMapper

    @Binds
    fun bindsViewHistoryEntityMapperEx(impl: ViewHistoryEntityMapperExImpl): ViewHistoryEntityMapperEx

    @Binds
    fun bindsViewHistoryRepository(impl: ViewHistoryRepositoryImpl): ViewHistoryRepository

    // domain

    @Binds
    fun bindsViewHistoryInteractor(impl: ViewHistoryInteractorImpl): ViewHistoryInteractor

    @Binds
    fun bindsViewHistoryProgressUseCase(impl: ViewHistoryProgressUseCaseImpl): ViewHistoryProgressUseCase

    // presentation

    @Binds
    fun bindsViewHistoryItemUiModelMapper(impl: ViewHistoryItemUiModelMapperImpl): ViewHistoryItemUiModelMapper

    companion object {
        @Provides
        @JvmStatic
        fun providesViewHistoryDao(db: MyRoomDb): ViewHistoryDao {
            return db.viewHistoryDao()
        }
    }
}