package ru.samtakoy.presentation.base.viewmodel.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SavedStateValue<T>(
    private val initialValueGetter: () -> T,
    private val keyName: String,
    private val savedStateHandle: SavedStateHandle,
    private val serialize: (T) -> String,
    private val deserialize: (String) -> T,
    saveScope: CoroutineScope
) {
    /** Текущее состояние */
    private val stateFlow = MutableStateFlow(readValue())

    var value: T
        get() = stateFlow.value
        set(newValue) {
            stateFlow.value = newValue
        }

    init {
        @OptIn(FlowPreview::class)
        stateFlow
            .debounce(SAVE_DELAY)
            .onEach { value ->
                saveValue(value)
            }
            .launchIn(saveScope)
    }

    fun asFlow(): Flow<T> {
        return stateFlow
    }

    private fun readValue(): T {
        return deserialize(savedStateHandle.get<String>(keyName) ?: serialize(initialValueGetter()))
    }

    private fun saveValue(value: T) {
        savedStateHandle.set<Any>(keyName, serialize(value))
    }

    companion object {
        private const val SAVE_DELAY = 10L
    }
}