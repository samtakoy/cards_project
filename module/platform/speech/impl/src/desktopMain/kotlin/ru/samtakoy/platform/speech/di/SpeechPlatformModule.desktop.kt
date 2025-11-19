package ru.samtakoy.platform.speech.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.platform.speech.PlayCardsTask
import ru.samtakoy.platform.speech.PlayCardsTaskImpl

actual fun speechPlatformModule(): Module = module {
    singleOf(::PlayCardsTaskImpl) bind PlayCardsTask::class
}