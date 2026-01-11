package ru.samtakoy.presentation.qpacks.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.utils.scope.releaseScope
import ru.samtakoy.common.utils.scope.useScope
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
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoPlayDialogMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoPlayDialogMapperImpl
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModelImpl
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListItemUiModelMapperImpl
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListSortButtonMapper
import ru.samtakoy.presentation.qpacks.screens.list.mapper.QPackListSortButtonMapperImpl
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModelImpl
import ru.samtakoy.presentation.themes.entry.QPackListEntryImpl
import ru.samtakoy.speech.domain.scope.PlayerScopeQualifier

fun qPackPresentationModule() = module {
    // info
    factoryOf(::QPackInfoPlayDialogMapperImpl) bind QPackInfoPlayDialogMapper::class
    factoryOf(::QPackInfoDialogMapperImpl) bind QPackInfoDialogMapper::class
    factoryOf(::QPackInfoButtonsMapperImpl) bind QPackInfoButtonsMapper::class
    factoryOf(::QPackInfoMenuMapperImpl) bind QPackInfoMenuMapper::class
    viewModel {
        val playerScope = getKoin().useScope(PlayerScopeQualifier)
        QPackInfoViewModelImpl(
            cardInteractor = get(),
            qPackInteractor = get(),
            favoritesInteractor = get(),
            coursesInteractor = get(),
            viewHistoryInteractor = get(),
            playCardsTask = playerScope.get(),
            cardsMapper = get(),
            buttonsMapper = get(),
            choiceDialogMapper = get(),
            playChoiceDialogMapper = get(),
            toolbarMenuMapper = get(),
            scopeProvider = get(),
            qPackId = get()
        ) {
            playerScope.releaseScope()
        }
    }
    factory<RootFeatureEntry>(qualifier = named<QPackInfoRoute>()) {
        QPackInfoEntryImpl()
    }


    // БШ-список
    factoryOf(::FastCardUiModelMapperImpl) bind FastCardUiModelMapper::class

    // список
    factory<MainTabFeatureEntry>(named<QPackListRoute>()) { QPackListEntryImpl() }
    factoryOf(::QPackListItemUiModelMapperImpl) bind QPackListItemUiModelMapper::class
    factoryOf(::QPackListSortButtonMapperImpl) bind QPackListSortButtonMapper::class
    viewModelOf(::QPackListViewModelImpl)

    // выбор
    factory<MainTabFeatureEntry>(named< QPackSelectionRoute>()) { QPackSelectionEntryImpl() }
}