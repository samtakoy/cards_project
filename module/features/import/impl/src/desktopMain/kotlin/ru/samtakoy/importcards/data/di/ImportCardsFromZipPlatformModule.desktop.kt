package ru.samtakoy.importcards.data.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.importcards.domain.batch.ImportCardsFromZipTask
import ru.samtakoy.importcards.data.ImportCardsFromZipTaskDesktopImpl

actual fun importCardsFromZipPlatformModule(): Module = module {
    singleOf(::ImportCardsFromZipTaskDesktopImpl) bind ImportCardsFromZipTask::class
}