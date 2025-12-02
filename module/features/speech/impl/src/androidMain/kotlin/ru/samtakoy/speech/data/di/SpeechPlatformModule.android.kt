package ru.samtakoy.speech.data.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.speech.data.PlayCardsTaskImpl
import ru.samtakoy.speech.domain.PlayCardsTask

actual fun speechPlatformModule(): Module = module {
    singleOf(::PlayCardsTaskImpl) bind PlayCardsTask::class
}