package ru.samtakoy.speech.data.di

import android.content.Context
import nl.marc_apps.tts.TextToSpeechEngine
import nl.marc_apps.tts.TextToSpeechFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun speechPlatformDataModule(): Module = module {
    factory {
        TextToSpeechFactory(get<Context>(), TextToSpeechEngine.Google)
    }
}