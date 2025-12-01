package ru.samtakoy.data.speech.di

import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.speech.TextToSpeechRepository
import ru.samtakoy.data.speech.TextToSpeechRepositoryImpl
import ru.samtakoy.domain.speech.scope.PlayerScopeQualifier

fun speechDataModule() = module {
    scope(qualifier = PlayerScopeQualifier) {
        @OptIn(ExperimentalDesktopTarget::class)
        scopedOf(::TextToSpeechRepositoryImpl) bind TextToSpeechRepository::class
    }
}