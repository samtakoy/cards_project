package ru.samtakoy.importcards.platform.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.importcards.domain.batch.RunImportCardsFromZipTaskUseCase
import ru.samtakoy.importcards.platform.RunImportCardsFromZipTaskUseCaseAndroidImpl

actual fun importCardsFromZipPlatformModule() = module {
    singleOf(::RunImportCardsFromZipTaskUseCaseAndroidImpl) bind RunImportCardsFromZipTaskUseCase::class
}