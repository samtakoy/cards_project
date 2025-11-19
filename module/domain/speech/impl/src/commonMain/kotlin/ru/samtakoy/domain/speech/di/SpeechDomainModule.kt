package ru.samtakoy.domain.speech.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.domain.speech.PlayCardsAudioUseCase
import ru.samtakoy.domain.speech.PlayCardsAudioUseCaseImpl
import ru.samtakoy.domain.speech.TextToSpeechUseCase
import ru.samtakoy.domain.speech.TextToSpeechUseCaseImpl
import ru.samtakoy.domain.speech.model.SpeechControlsListener

fun speechDomainModule() = module{
    factoryOf(::TextToSpeechUseCaseImpl) bind TextToSpeechUseCase::class
    factory<PlayCardsAudioUseCase> {
        PlayCardsAudioUseCaseImpl(
            dispatcher = get(qualifier = named<SpeechControlsListener>()),
            cardsRepository = get(),
            textToSpeechRepository = get(),
            scopeProvider = get()
        )
    }

    // диспатчер для событий управления текущим плейером
    single(qualifier = named<SpeechControlsListener>()) {
        Dispatcher<SpeechControlsListener>()
    }
}