package ru.samtakoy.oldlegacy.di.modules

import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.dsl.module

// TODO не используется?
fun workerFactoryModule() = module {
    single { KoinWorkerFactory() }
}