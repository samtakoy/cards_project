package ru.samtakoy.speech.data.di

import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.speech.domain.TextToSpeechRepository
import ru.samtakoy.speech.data.TextToSpeechRepositoryImpl
import ru.samtakoy.speech.domain.scope.PlayerScopeQualifier

fun speechDataModule() = module {
    scope(qualifier = PlayerScopeQualifier) {
        @OptIn(ExperimentalDesktopTarget::class)
        scopedOf(::TextToSpeechRepositoryImpl) bind TextToSpeechRepository::class
    }
}