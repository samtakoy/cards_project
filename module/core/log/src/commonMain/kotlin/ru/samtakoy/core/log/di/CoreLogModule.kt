package ru.samtakoy.core.log.di

import org.koin.dsl.module
import ru.samtakoy.core.log.CustomLogger
import ru.samtakoy.core.log.CustomLoggerImpl
import ru.samtakoy.common.utils.log.MyLog

fun coreLogModule() = module {
    single<CustomLogger> { CustomLoggerImpl(MyLog.tag) }
}