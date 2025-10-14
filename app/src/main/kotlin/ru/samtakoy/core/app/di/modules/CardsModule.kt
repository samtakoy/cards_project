package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.favorites.FavoritesInteractorImpl
import ru.samtakoy.domain.theme.ThemeInteractor
import ru.samtakoy.domain.theme.ThemeInteractorImpl

@Module
internal interface CardsModule {
    @Binds
    fun bindsThemeInteractor(impl: ThemeInteractorImpl): ThemeInteractor

    @Binds
    fun bindsFavoritesInteractor(impl: FavoritesInteractorImpl): FavoritesInteractor
}
