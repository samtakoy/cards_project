package ru.samtakoy.speech.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.speech.domain.PlayCardsAudioUseCase
import ru.samtakoy.speech.domain.PlayCardsAudioUseCaseImpl
import ru.samtakoy.speech.domain.ReadSpeechPlayerStateUseCase
import ru.samtakoy.speech.domain.ReadSpeechPlayerStateUseCaseImpl
import ru.samtakoy.speech.domain.mapper.PlayerAudioMapper
import ru.samtakoy.speech.domain.mapper.PlayerAudioMapperImpl
import ru.samtakoy.speech.domain.model.SpeechControlsListener
import ru.samtakoy.speech.domain.scope.PlayerScopeQualifier
import ru.samtakoy.speech.domain.scope.PlayerStateScopeQualifier

fun speechDomainModule() = module {

    factoryOf(::PlayerAudioMapperImpl) bind PlayerAudioMapper::class

    scope(qualifier = PlayerScopeQualifier) {
        scopedOf(::PlayCardsAudioUseCaseImpl) bind PlayCardsAudioUseCase::class
    }

    scope(qualifier = PlayerStateScopeQualifier) {
        // диспатчер для событий управления текущим плейером
        scoped {
            Dispatcher<SpeechControlsListener>()
        }
        scopedOf(::ReadSpeechPlayerStateUseCaseImpl) bind ReadSpeechPlayerStateUseCase::class
    }
}