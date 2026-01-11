package ru.samtakoy.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.samtakoy.common.utils.coroutines.ScopeProvider

abstract class BaseViewModelImpl<State: Any, Action, Event>(
    private val scopeProvider: ScopeProvider,
    initialState: State
) : ViewModel(), BaseViewModel<State, Action, Event>, ViewModelScope {

    override val mainScope: CoroutineScope = scopeProvider.mainScope
    override val ioScope: CoroutineScope = scopeProvider.ioScope

    private val _viewStates = MutableStateFlow(initialState)
    private val _viewActions = MutableSharedFlow<Action>(
        replay = 0,
        extraBufferCapacity = ACTIONS_BUFFER_EXTRA_CAPACITY,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    private var isFirstTimeInit: Boolean = true

    @Deprecated(
        "Возможны гонки при изменении и не корректный результат. Используй для обновления [updateState()]." +
        " Этот метод только для чтения"
    )
    protected var viewState: State
        get() = _viewStates.value
        set(value) {
            _viewStates.update { value }
        }

    suspend fun updateState(function: suspend (State) -> State) {
        _viewStates.update { state -> function(state) }
    }

    fun updateStateSync(function: (State) -> State) {
        _viewStates.update(function)
    }

    @Deprecated("осталось от реализации на фрагментах")
    override fun onViewCreated() {
        if (isFirstTimeInit) {
            isFirstTimeInit = false
            onInit()
        }
    }

    override fun onCleared() {
        scopeProvider.cancel()
        super.onCleared()
    }

    override fun getViewStateAsFlow(): StateFlow<State> = _viewStates.asStateFlow()

    override fun getViewActionsAsFlow(): Flow<Action> = _viewActions.filterNotNull()

    protected open fun onInit() = Unit

    protected fun sendAction(action: Action) {
        mainScope.launch {
            _viewActions.emit(action)
        }
    }

    companion object {
        private const val ACTIONS_BUFFER_EXTRA_CAPACITY = 3
    }
}