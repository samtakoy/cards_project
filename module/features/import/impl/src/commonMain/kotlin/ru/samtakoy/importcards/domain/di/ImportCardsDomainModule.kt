package ru.samtakoy.importcards.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.importcards.domain.QPackBuilderInteractorImpl
import ru.samtakoy.importcards.domain.ImportCardsZipUseCaseImpl
import ru.samtakoy.importcards.domain.batch.ImportCardsZipUseCase

fun importCardsDomainModule() = module {
    factoryOf(::ImportCardsZipUseCaseImpl) bind ImportCardsZipUseCase::class
    factoryOf(::QPackBuilderInteractorImpl)
}