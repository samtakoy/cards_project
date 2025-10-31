package ru.samtakoy.data.importcards.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.importcards.CardsSourceRepository
import ru.samtakoy.data.importcards.CardsSourceRepositoryImpl

fun importDataModule() = module {
    factoryOf(::CardsSourceRepositoryImpl) bind CardsSourceRepository::class
}