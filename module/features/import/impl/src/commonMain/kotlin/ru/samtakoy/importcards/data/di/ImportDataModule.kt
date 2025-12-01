package ru.samtakoy.importcards.data.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.importcards.domain.CardsSourceRepository
import ru.samtakoy.importcards.data.CardsSourceRepositoryImpl

fun importDataModule() = module {
    factoryOf(::CardsSourceRepositoryImpl) bind CardsSourceRepository::class
}