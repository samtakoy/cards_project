package ru.samtakoy.oldlegacy.core.presentation.favorites.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapper
import ru.samtakoy.oldlegacy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapperImpl
import ru.samtakoy.oldlegacy.core.presentation.favorites.onboarding.vm.FavoritesViewModelImpl
import ru.samtakoy.oldlegacy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapper
import ru.samtakoy.oldlegacy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapperImpl
import ru.samtakoy.oldlegacy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModelImpl
import ru.samtakoy.presentation.favorites.FavoritesRoute
import ru.samtakoy.presentation.favorites.entry.FavoritesEntryImpl
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry

internal fun favoritesPresentationModule() = module {
    factory<MainTabFeatureEntry>(named<FavoritesRoute>()) { FavoritesEntryImpl() }

    factoryOf(::QPacksWithFavsItemsMapperImpl) bind QPacksWithFavsItemsMapper::class
    viewModelOf(::QPackSelectionViewModelImpl)

    factoryOf(::FavoritesButtonsMapperImpl) bind FavoritesButtonsMapper::class
    viewModelOf(::FavoritesViewModelImpl)
}