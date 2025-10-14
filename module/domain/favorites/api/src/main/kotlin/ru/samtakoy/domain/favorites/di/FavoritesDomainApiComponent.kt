package ru.samtakoy.domain.favorites.di

import ru.samtakoy.domain.favorites.FavoritesInteractor

interface FavoritesDomainApiComponent {
    fun favoritesInteractor(): FavoritesInteractor
}