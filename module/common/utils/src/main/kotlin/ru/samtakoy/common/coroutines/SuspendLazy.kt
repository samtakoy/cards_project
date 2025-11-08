package ru.samtakoy.common.coroutines

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SuspendLazy<T>(private val initializer: suspend () -> T) {
    private val mutex = Mutex()
    private var _value: Any? = UNINITIALIZED_VALUE

    suspend fun getValue(): T {
        if (_value !== UNINITIALIZED_VALUE) {
            @Suppress("UNCHECKED_CAST")
            return _value as T
        }
        return mutex.withLock {
            if (_value === UNINITIALIZED_VALUE) {
                _value = initializer()
            }
            @Suppress("UNCHECKED_CAST")
            _value as T
        }
    }

    companion object {
        private object UNINITIALIZED_VALUE
    }
}