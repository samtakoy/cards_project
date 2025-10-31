package ru.samtakoy.core.presentation.favorites.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapper
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapperImpl
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModelImpl
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapper
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapperImpl
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModelImpl
import ru.samtakoy.presentation.favorites.FavoritesRoute
import ru.samtakoy.presentation.favorites.entry.FavoritesEntryImpl
import ru.samtakoy.presentation.navigation.MainFeatureEntry

internal fun favoritesPresentationModule() = module {
    factory<MainFeatureEntry>(named<FavoritesRoute>()) { FavoritesEntryImpl(get()) }

    factoryOf(::QPacksWithFavsItemsMapperImpl) bind QPacksWithFavsItemsMapper::class
    viewModelOf(::QPackSelectionViewModelImpl)

    factoryOf(::FavoritesButtonsMapperImpl) bind FavoritesButtonsMapper::class
    viewModelOf(::FavoritesViewModelImpl)
}