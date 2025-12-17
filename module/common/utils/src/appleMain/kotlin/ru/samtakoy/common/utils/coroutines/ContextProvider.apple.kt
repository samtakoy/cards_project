package ru.samtakoy.common.utils.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual fun createContextProvider(): ContextProvider {
    return object : ContextProvider {
        override val main = Dispatchers.Main
        override val io = Dispatchers.IO
        override val calc = Dispatchers.Default
    }
}