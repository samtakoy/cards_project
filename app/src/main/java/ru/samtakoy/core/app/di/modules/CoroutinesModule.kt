package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.app.ContextProvider
import ru.samtakoy.core.app.ContextProviderImpl

@Module
interface CoroutinesModule {
    @Binds
    fun provideContextProvider(impl: ContextProviderImpl): ContextProvider
}