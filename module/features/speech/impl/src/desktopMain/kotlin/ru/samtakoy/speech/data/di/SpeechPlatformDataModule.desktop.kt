package ru.samtakoy.speech.data.di

import nl.marc_apps.tts.TextToSpeechFactory
import nl.marc_apps.tts.experimental.ExperimentalDesktopTarget
import org.koin.core.module.Module
import org.koin.dsl.module


actual fun speechPlatformDataModule(): Module = module {
    @OptIn(ExperimentalDesktopTarget::class)
    factory {
        TextToSpeechFactory()
    }
}