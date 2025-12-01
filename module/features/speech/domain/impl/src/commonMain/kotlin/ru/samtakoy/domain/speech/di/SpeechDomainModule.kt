package ru.samtakoy.domain.speech.di

import org.koin.dsl.module
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.domain.speech.PlayCardsAudioUseCase
import ru.samtakoy.domain.speech.PlayCardsAudioUseCaseImpl
import ru.samtakoy.domain.speech.model.SpeechControlsListener
import ru.samtakoy.domain.speech.scope.PlayerScopeQualifier
import ru.samtakoy.domain.speech.scope.PlayerStateScopeQualifier

fun speechDomainModule() = module {

    scope(qualifier = PlayerScopeQualifier) {
        scoped<PlayCardsAudioUseCase> {
            PlayCardsAudioUseCaseImpl(
                dispatcher = getScope(scopeID = PlayerStateScopeQualifier.value).get(),
                cardsRepository = get(),
                textToSpeechRepository = get(),
                scopeProvider = get()
            )
        }
    }

    // диспатчер для событий управления текущим плейером
    scope(qualifier = PlayerStateScopeQualifier) {
        scoped {
            Dispatcher<SpeechControlsListener>()
        }
    }
}