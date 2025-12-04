package ru.samtakoy.speech.platform.di

import android.content.Context
import nl.marc_apps.tts.TextToSpeechEngine
import nl.marc_apps.tts.TextToSpeechFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.speech.platform.RunPlayCardsTaskUseCaseImpl
import ru.samtakoy.speech.domain.RunPlayCardsTaskUseCase
import ru.samtakoy.speech.platform.mapper.PlayerNotificationTextMapper
import ru.samtakoy.speech.platform.mapper.PlayerNotificationTextMapperImpl

actual fun speechPlatformModule(): Module = module {
    factoryOf(::PlayerNotificationTextMapperImpl) bind PlayerNotificationTextMapper::class
    singleOf(::RunPlayCardsTaskUseCaseImpl) bind RunPlayCardsTaskUseCase::class
    factory {
        TextToSpeechFactory(get<Context>(), TextToSpeechEngine.Google)
    }
}