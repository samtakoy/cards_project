package ru.samtakoy.platform.importcards.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.platform.importcards.ImportCardsFromZipTask
import ru.samtakoy.platform.importcards.ImportCardsFromZipTaskAndroidImpl

actual fun importCardsFromZipPlatformModule() = module {
    singleOf(::ImportCardsFromZipTaskAndroidImpl) bind ImportCardsFromZipTask::class
}