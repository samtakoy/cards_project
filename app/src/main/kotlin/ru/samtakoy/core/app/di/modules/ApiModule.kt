package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.app.di.components.AppSingletonScope
import ru.samtakoy.features.preferences.data.AppPreferences
import ru.samtakoy.features.preferences.data.AppPreferencesImpl
import javax.inject.Singleton

@Module
abstract class ApiModule {


    @AppSingletonScope
    @Binds
    abstract fun provideAppPreferences(impl: AppPreferencesImpl): AppPreferences
}