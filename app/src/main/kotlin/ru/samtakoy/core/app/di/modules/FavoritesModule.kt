package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapper
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapperImpl
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapper
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapperImpl

@Module
internal interface FavoritesModule {
    @Binds
    fun bindsQPacksWithFavsItemsMapper(impl: QPacksWithFavsItemsMapperImpl): QPacksWithFavsItemsMapper
    @Binds
    fun bindsFavoritesButtonsMapper(impl: FavoritesButtonsMapperImpl): FavoritesButtonsMapper
}