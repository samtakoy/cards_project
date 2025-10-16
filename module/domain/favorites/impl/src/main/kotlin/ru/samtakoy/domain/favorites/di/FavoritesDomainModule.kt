package ru.samtakoy.domain.favorites.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.favorites.FavoritesInteractorImpl

fun favoritesDomainModule() = module {
    factoryOf(::FavoritesInteractorImpl) bind FavoritesInteractor::class
}