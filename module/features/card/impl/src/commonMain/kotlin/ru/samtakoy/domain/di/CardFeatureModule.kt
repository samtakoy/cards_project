package ru.samtakoy.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.card.CardInteractorImpl
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.domain.cardtag.TagInteractorImpl

fun cardFeatureModule() = module {
    factoryOf(::CardInteractorImpl) bind CardInteractor::class
    factoryOf(::TagInteractorImpl) bind TagInteractor::class
}