package ru.samtakoy.common.utils.coroutines

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext


internal actual fun createContextProvider(): ContextProvider {
    return object : ContextProvider {
        override val main: CoroutineContext by lazy { Dispatchers.Main }
        override val io by lazy {
            @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
            newSingleThreadContext("IO")
        }
        override val calc: CoroutineContext by lazy { Dispatchers.Default }
    }
}