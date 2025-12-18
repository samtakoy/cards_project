package ru.samtakoy.common.utils.coroutines

import kotlin.coroutines.CoroutineContext

interface ContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
    val calc: CoroutineContext
}

internal expect fun createContextProvider(): ContextProvider
