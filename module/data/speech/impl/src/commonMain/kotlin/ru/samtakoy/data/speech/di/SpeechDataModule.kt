package ru.samtakoy.data.speech.di

import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.data.speech.TextToSpeechRepository
import ru.samtakoy.data.speech.TextToSpeechRepositoryImpl


fun speechDataModule() = module {
    @OptIn(ExperimentalDesktopTarget::class)
    factoryOf(::TextToSpeechRepositoryImpl) bind TextToSpeechRepository::class
}