package ru.samtakoy.speech.platform.di

import nl.marc_apps.tts.TextToSpeechFactory
import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.speech.domain.RunPlayCardsTaskUseCase
import ru.samtakoy.speech.platform.RunPlayCardsTaskUseCaseImpl

actual fun speechPlatformModule(): Module = module {
    singleOf(::RunPlayCardsTaskUseCaseImpl) bind RunPlayCardsTaskUseCase::class
    @OptIn(ExperimentalDesktopTarget::class)
    factory {
        TextToSpeechFactory()
    }
}