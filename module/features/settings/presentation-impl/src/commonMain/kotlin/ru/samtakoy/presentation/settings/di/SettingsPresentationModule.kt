package ru.samtakoy.presentation.settings.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.presentation.settings.SettingsRoute
import ru.samtakoy.presentation.themes.entry.SettingsEntryImpl

fun settingsPresentationModule() = module {
    factory<MainTabFeatureEntry>(named<SettingsRoute>()) { SettingsEntryImpl() }
}