package ru.samtakoy.data.remote.di

import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

fun remoteModule() = module {
    single(qualifier = NoBaseUrlKtorfit) {
        Ktorfit.Builder().build()
    }
}