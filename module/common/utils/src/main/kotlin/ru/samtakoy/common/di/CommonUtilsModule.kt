package ru.samtakoy.common.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.coroutines.ContextProvider
import ru.samtakoy.common.coroutines.ContextProviderImpl
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.resources.ResourcesImpl

fun commonUtilsModule() = module {
    factoryOf(::ContextProviderImpl) bind ContextProvider::class
    factoryOf(::ScopeProvider)
    factoryOf(::ResourcesImpl) bind Resources::class
    single<Gson> { GsonBuilder().create() }
}