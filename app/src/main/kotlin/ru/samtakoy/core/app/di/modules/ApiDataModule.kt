package ru.samtakoy.core.app.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.features.preferences.data.AppPreferences
import ru.samtakoy.features.preferences.data.AppPreferencesImpl

internal fun apiDataModule() = module {
    singleOf(::AppPreferencesImpl) bind AppPreferences::class
}