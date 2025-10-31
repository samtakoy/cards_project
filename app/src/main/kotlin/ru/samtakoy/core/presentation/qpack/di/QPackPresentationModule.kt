package ru.samtakoy.core.presentation.qpack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapperImpl
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModelImpl
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapperImpl
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModelImpl
import ru.samtakoy.presentation.navigation.MainFeatureEntry
import ru.samtakoy.presentation.qpacks.QPackListRoute
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute
import ru.samtakoy.presentation.qpacks.entry.QPackSelectionEntryImpl
import ru.samtakoy.presentation.themes.entry.QPackListEntryImpl

internal fun qPackPresentationModule() = module {
    factoryOf(::FastCardUiModelMapperImpl) bind FastCardUiModelMapper::class
    viewModelOf(::QPackInfoViewModelImpl)

    // список
    factory<MainFeatureEntry>(named< QPackListRoute>()) { QPackListEntryImpl(get()) }
    factoryOf(::QPackListItemUiModelMapperImpl) bind QPackListItemUiModelMapper::class
    viewModelOf(::QPacksListViewModelImpl)

    // выбор
    factory<MainFeatureEntry>(named< QPackSelectionRoute>()) { QPackSelectionEntryImpl(get()) }
}