package ru.samtakoy.domain.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.card.CardInteractorImpl
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.domain.cardtag.TagInteractorImpl

@Module
internal interface CardDomainModule {
    @Binds
    fun bindsCardsInteractor(impl: CardInteractorImpl): CardInteractor
    @Binds
    fun bindsTagInteractor(impl: TagInteractorImpl): TagInteractor
}