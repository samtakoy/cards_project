package ru.samtakoy.common.utils.di

import org.koin.core.module.dsl.factoryOf

import org.koin.dsl.module
import ru.samtakoy.common.utils.coroutines.ContextProvider
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.utils.coroutines.createContextProvider

fun commonUtilsModule() = module {

    factory<ContextProvider> { createContextProvider() }
    factoryOf(::ScopeProvider)
}