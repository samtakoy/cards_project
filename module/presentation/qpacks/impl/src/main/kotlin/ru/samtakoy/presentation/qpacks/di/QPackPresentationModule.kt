package ru.samtakoy.presentation.qpacks.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.RootFeatureEntry
import ru.samtakoy.presentation.qpacks.QPackInfoRoute
import ru.samtakoy.presentation.qpacks.QPackListRoute
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute
import ru.samtakoy.presentation.qpacks.entry.QPackInfoEntryImpl
import ru.samtakoy.presentation.qpacks.entry.QPackSelectionEntryImpl
import ru.samtakoy.presentation.qpacks.screens.fastlist.mapper.FastCardUiModelMapper
import ru.samtakoy.presentation.qpacks.screens.fastlist.mapper.FastCardUiModelMapperImpl
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapperImpl
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoDialogMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoDialogMapperImpl
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoMenuMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoMenuMapperImpl
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModelImpl
import ru.samtakoy.presentation.themes.entry.QPackListEntryImpl

fun qPackPresentationModule() = module {
    // info
    factoryOf(::QPackInfoDialogMapperImpl) bind QPackInfoDialogMapper::class
    factoryOf(::QPackInfoButtonsMapperImpl) bind QPackInfoButtonsMapper::class
    factoryOf(::QPackInfoMenuMapperImpl) bind QPackInfoMenuMapper::class
    viewModelOf(::QPackInfoViewModelImpl)
    factoryOf(::QPackInfoEntryImpl) {
        named<QPackInfoRoute>()
        bind<RootFeatureEntry>()
    }

    // БШ-список
    factoryOf(::FastCardUiModelMapperImpl) bind FastCardUiModelMapper::class

    // список
    factory<MainTabFeatureEntry>(named<QPackListRoute>()) { QPackListEntryImpl(get()) }

    // выбор
    factory<MainTabFeatureEntry>(named< QPackSelectionRoute>()) { QPackSelectionEntryImpl(get()) }
}