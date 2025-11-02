package ru.samtakoy.features.views.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapperImpl
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModelImpl
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.themes.entry.ViewsHistoryEntryImpl
import ru.samtakoy.presentation.viewshistory.ViewsHistoryRoute

internal fun viewsFeatureModule() = module {
    // presentation
    factory<MainTabFeatureEntry>(named<ViewsHistoryRoute>()) { ViewsHistoryEntryImpl(get()) }

    factoryOf(::ViewHistoryItemUiModelMapperImpl) bind ViewHistoryItemUiModelMapper::class
    viewModelOf(::ViewsHistoryViewModelImpl)
}