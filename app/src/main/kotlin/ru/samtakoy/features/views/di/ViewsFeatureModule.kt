package ru.samtakoy.features.views.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryProgressUseCase
import ru.samtakoy.features.views.domain.ViewHistoryInteractorImpl
import ru.samtakoy.features.views.domain.ViewHistoryProgressUseCaseImpl
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapperImpl

@Module
internal interface ViewsFeatureModule {

    // domain

    @Binds
    fun bindsViewHistoryInteractor(impl: ViewHistoryInteractorImpl): ViewHistoryInteractor

    @Binds
    fun bindsViewHistoryProgressUseCase(impl: ViewHistoryProgressUseCaseImpl): ViewHistoryProgressUseCase

    // presentation

    @Binds
    fun bindsViewHistoryItemUiModelMapper(impl: ViewHistoryItemUiModelMapperImpl): ViewHistoryItemUiModelMapper
}