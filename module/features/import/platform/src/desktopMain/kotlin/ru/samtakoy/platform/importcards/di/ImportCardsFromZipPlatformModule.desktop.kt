package ru.samtakoy.platform.importcards.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.platform.importcards.ImportCardsFromZipTask
import ru.samtakoy.platform.importcards.ImportCardsFromZipTaskDesktopImpl

actual fun importCardsFromZipPlatformModule(): Module = module {
    singleOf(::ImportCardsFromZipTaskDesktopImpl) bind ImportCardsFromZipTask::class
}