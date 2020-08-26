package ru.samtakoy.core.api.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.api.AppPreferences
import ru.samtakoy.core.api.AppPreferencesImpl

import javax.inject.Singleton

@Module
abstract class ApiModule {


    @Singleton
    @Binds
    abstract fun providesAppPreferences(impl: AppPreferencesImpl): AppPreferences

}