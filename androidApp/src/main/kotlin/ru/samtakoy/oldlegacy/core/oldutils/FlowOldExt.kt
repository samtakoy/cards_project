package ru.samtakoy.oldlegacy.core.oldutils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

inline fun <T> Flow<T?>.observe(lifecycle: LifecycleOwner, crossinline block: suspend (T) -> Unit) {
    lifecycle.lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            filterNotNull().collect { block(it) }
        }
    }
}