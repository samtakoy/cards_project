package ru.samtakoy.core.app.di.modules

import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.dsl.module

fun workerFactoryModule() = module {
    single { KoinWorkerFactory() }
}