package ru.samtakoy.oldlegacy.di.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.features.preferences.data.AppPreferences
import ru.samtakoy.oldlegacy.features.preferences.data.AppPreferencesImpl

internal fun apiDataModule() = module {
    singleOf(::AppPreferencesImpl) bind AppPreferences::class
}