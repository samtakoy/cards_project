package ru.samtakoy.common.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.io.Closeable

class ScopeProvider(
    contextProvider: ContextProvider
) : Closeable {
    private var job = SupervisorJob()
    val isCancelled: Boolean
        get() = job.isCancelled

    var mainScope = CoroutineScope(contextProvider.main + job)
        private set
    var ioScope = CoroutineScope(contextProvider.io + job)
        private set

    override fun close() {
        cancel()
    }

    fun cancel() {
        job.cancel()
    }
}