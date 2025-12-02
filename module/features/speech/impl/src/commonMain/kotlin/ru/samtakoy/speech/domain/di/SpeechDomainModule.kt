package ru.samtakoy.speech.domain.di

import org.koin.dsl.module
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.speech.domain.PlayCardsAudioUseCase
import ru.samtakoy.speech.domain.PlayCardsAudioUseCaseImpl
import ru.samtakoy.speech.domain.model.SpeechControlsListener
import ru.samtakoy.speech.domain.scope.PlayerScopeQualifier
import ru.samtakoy.speech.domain.scope.PlayerStateScopeQualifier

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