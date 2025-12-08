package ru.samtakoy.common.utils.di

import org.koin.core.module.dsl.factoryOf

import org.koin.dsl.module
import ru.samtakoy.common.utils.coroutines.ContextProvider
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.utils.coroutines.createContextProvider
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.common.utils.log.CustomLoggerImpl
import ru.samtakoy.common.utils.log.MyLog

fun commonUtilsModule() = module {
    single<CustomLogger> { CustomLoggerImpl(MyLog.tag) }
    factory<ContextProvider> { createContextProvider() }
    factoryOf(::ScopeProvider)
}