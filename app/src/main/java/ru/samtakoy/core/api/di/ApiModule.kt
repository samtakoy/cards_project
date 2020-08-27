package ru.samtakoy.core.api.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.core.api.AppPreferences
import ru.samtakoy.core.api.AppPreferencesImpl

import javax.inject.Singleton

@Module
abstract class ApiModule {


    @Singleton
    @Binds
    abstract fun provideAppPreferences(impl: AppPreferencesImpl): AppPreferences

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideGson(): Gson {
            return GsonBuilder().create();
        }
    }


}