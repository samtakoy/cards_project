package ru.samtakoy.domain.favorites.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.favorites.FavoritesInteractorImpl

@Module
internal interface FavoritesDomainModule {
    @Binds
    fun bindsFavoritesInteractor(impl: FavoritesInteractorImpl): FavoritesInteractor
}