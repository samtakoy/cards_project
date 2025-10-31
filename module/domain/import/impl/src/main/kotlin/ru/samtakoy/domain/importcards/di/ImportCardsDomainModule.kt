package ru.samtakoy.domain.importcards.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.domain.importcards.QPackBuilderInteractorImpl
import ru.samtakoy.domain.importcards.ImportCardsZipUseCaseImpl
import ru.samtakoy.domain.importcards.batch.ImportCardsZipUseCase

fun importCardsDomainModule() = module {
    factoryOf(::ImportCardsZipUseCaseImpl) bind ImportCardsZipUseCase::class
    factoryOf(::QPackBuilderInteractorImpl)
}