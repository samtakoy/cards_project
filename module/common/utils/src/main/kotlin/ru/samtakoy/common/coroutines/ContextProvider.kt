package ru.samtakoy.common.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import javax.inject.Inject

interface ContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
    val calc: CoroutineContext
}

internal class ContextProviderImpl @Inject constructor() : ContextProvider {
    override val main: CoroutineContext by lazy { Dispatchers.Main }
    override val io: CoroutineContext by lazy { Dispatchers.IO }
    override val calc: CoroutineContext by lazy { Dispatchers.Default }
}