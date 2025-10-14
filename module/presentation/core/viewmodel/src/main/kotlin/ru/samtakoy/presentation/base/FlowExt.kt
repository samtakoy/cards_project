package ru.samtakoy.presentation.base

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <T> Flow<T?>.observe(lifecycle: LifecycleOwner, crossinline block: suspend (T) -> Unit) {
    lifecycle.lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            filterNotNull().collect { block(it) }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.observeActionsWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit
) {
    DisposableEffect(Unit) {
        val job = flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
            .onEach(action)
            .launchIn(lifecycleOwner.lifecycleScope)

        onDispose {
            job.cancel()
        }
    }
}