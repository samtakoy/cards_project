package ru.samtakoy.presentation.base.viewmodel.savedstate

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
inline fun <reified T : Any?> MutableState<T>.toSavedState(
    keyName: String,
    savedStateHandle: SavedStateHandle,
    crossinline serialize: (T) -> Any,
    deserialize: (Any) -> T,
    saveScope: CoroutineScope
): MutableState<T> {
    val SAVE_DELAY = 10L

    // восстановление стейта
    savedStateHandle.get<Any>(keyName)?.let {
        Snapshot.withMutableSnapshot {
            value = deserialize(it)
        }
    }
    // сохранение стейта
    snapshotFlow { value }
        .distinctUntilChanged()
        .debounce(SAVE_DELAY)
        .onEach { value ->
            savedStateHandle.set<Any>(keyName, serialize(value))
        }
        .launchIn(saveScope)
    return this
}
