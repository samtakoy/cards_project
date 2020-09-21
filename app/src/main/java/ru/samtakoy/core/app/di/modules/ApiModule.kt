package ru.samtakoy.core.app.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.core.data.local.preferences.AppPreferences
import ru.samtakoy.core.data.local.preferences.AppPreferencesImpl

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