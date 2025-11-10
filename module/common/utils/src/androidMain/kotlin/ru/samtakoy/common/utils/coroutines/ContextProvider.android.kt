package ru.samtakoy.common.utils.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal actual fun createContextProvider(): ContextProvider {
    return object : ContextProvider {
        override val main: CoroutineContext by lazy { Dispatchers.Main }
        // на ios нет IO
        override val io: CoroutineContext by lazy { Dispatchers.IO }
        override val calc: CoroutineContext by lazy { Dispatchers.Default }
    }
}