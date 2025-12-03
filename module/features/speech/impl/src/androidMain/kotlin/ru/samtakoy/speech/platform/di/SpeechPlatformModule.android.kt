package ru.samtakoy.speech.platform.di

import android.content.Context
import nl.marc_apps.tts.TextToSpeechEngine
import nl.marc_apps.tts.TextToSpeechFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.speech.platform.RunPlayCardsTaskUseCaseImpl
import ru.samtakoy.speech.domain.RunPlayCardsTaskUseCase

actual fun speechPlatformModule(): Module = module {
    singleOf(::RunPlayCardsTaskUseCaseImpl) bind RunPlayCardsTaskUseCase::class
    factory {
        TextToSpeechFactory(get<Context>(), TextToSpeechEngine.Google)
    }
}