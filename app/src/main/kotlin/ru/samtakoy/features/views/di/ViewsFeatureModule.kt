package ru.samtakoy.features.views.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapperImpl
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModelImpl

internal fun viewsFeatureModule() = module {
    // presentation
    factoryOf(::ViewHistoryItemUiModelMapperImpl) bind ViewHistoryItemUiModelMapper::class
    viewModelOf(::ViewsHistoryViewModelImpl)
}