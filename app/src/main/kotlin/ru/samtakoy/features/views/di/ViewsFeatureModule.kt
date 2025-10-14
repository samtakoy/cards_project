package ru.samtakoy.features.views.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapperImpl

@Module
internal interface ViewsFeatureModule {
    // presentation
    @Binds
    fun bindsViewHistoryItemUiModelMapper(impl: ViewHistoryItemUiModelMapperImpl): ViewHistoryItemUiModelMapper
}