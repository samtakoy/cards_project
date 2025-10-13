package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.core.domain.impl.FavoritesInteractorImpl
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.theme.ThemeInteractor
import ru.samtakoy.features.card.domain.CardInteractorImpl
import ru.samtakoy.features.qpack.domain.QPackInteractorImpl
import ru.samtakoy.features.tag.domain.TagInteractorImpl
import ru.samtakoy.features.theme.domain.ThemeInteractorImpl

@Module
internal interface CardsModule {
    @Binds
    fun bindsCardsInteractor(impl: CardInteractorImpl): CardInteractor

    @Binds
    fun bindsQPackInteractor(impl: QPackInteractorImpl): QPackInteractor

    @Binds
    fun bindsThemeInteractor(impl: ThemeInteractorImpl): ThemeInteractor

    @Binds
    fun bindsTagInteractor(impl: TagInteractorImpl): TagInteractor

    @Binds
    fun bindsFavoritesInteractor(impl: FavoritesInteractorImpl): FavoritesInteractor
}
