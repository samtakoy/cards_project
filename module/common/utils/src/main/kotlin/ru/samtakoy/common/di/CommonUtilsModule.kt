package ru.samtakoy.common.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.resources.ResourcesImpl

@Module
internal interface CommonUtilsModule {
    companion object {
        @JvmStatic
        @Provides
        fun provideResources(resources: ResourcesImpl): Resources {
            return resources
        }

        @JvmStatic
        @Provides
        @CommonUtilsScope
        fun provideGson(): Gson {
            return GsonBuilder().create()
        }
    }
}