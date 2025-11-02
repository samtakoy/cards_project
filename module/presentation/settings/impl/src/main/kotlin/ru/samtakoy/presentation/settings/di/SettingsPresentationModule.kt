package ru.samtakoy.presentation.settings.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.settings.SettingsRoute
import ru.samtakoy.presentation.themes.entry.SettingsEntryImpl

fun settingsPresentationModule() = module {
    factory<MainTabFeatureEntry>(named<SettingsRoute>()) { SettingsEntryImpl(get()) }
}