package ru.samtakoy.common.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.aakira.napier.Antilog
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.common.coroutines.ContextProvider
import ru.samtakoy.common.coroutines.ContextProviderImpl
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.common.utils.log.CustomLoggerImpl
import ru.samtakoy.common.utils.log.MyLog

fun commonUtilsModule() = module {
    single<CustomLogger> { CustomLoggerImpl(MyLog.tag) }
    factoryOf(::ContextProviderImpl) bind ContextProvider::class
    factoryOf(::ScopeProvider)
    single<Gson> { GsonBuilder().create() }
}