package ru.samtakoy.importcards.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.importcards.domain.batch.ImportCardsFromZipTask
import ru.samtakoy.importcards.data.ImportCardsFromZipTaskAndroidImpl

actual fun importCardsFromZipPlatformModule() = module {
    singleOf(::ImportCardsFromZipTaskAndroidImpl) bind ImportCardsFromZipTask::class
}