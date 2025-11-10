package ru.samtakoy.common.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ScopeProvider(
    contextProvider: ContextProvider
) : AutoCloseable {
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