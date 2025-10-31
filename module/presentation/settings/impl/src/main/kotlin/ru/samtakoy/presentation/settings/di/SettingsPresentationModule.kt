package ru.samtakoy.presentation.settings.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.samtakoy.presentation.navigation.MainFeatureEntry
import ru.samtakoy.presentation.settings.SettingsRoute
import ru.samtakoy.presentation.themes.entry.SettingsEntryImpl

fun settingsPresentationModule() = module {
    factory<MainFeatureEntry>(named<SettingsRoute>()) { SettingsEntryImpl(get()) }
}