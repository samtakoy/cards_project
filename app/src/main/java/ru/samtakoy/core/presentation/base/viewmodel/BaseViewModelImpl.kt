package ru.samtakoy.core.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.features.import_export.CoursesExporter

abstract class BaseViewModelImpl<State: Any, Action, Event>(
    private val scopeProvider: ScopeProvider,
    initialState: State
) : ViewModel(), BaseViewModel<State, Action, Event>, ViewModelScope {

    override val mainScope: CoroutineScope = scopeProvider.mainScope
    override val ioScope: CoroutineScope = scopeProvider.ioScope

    private val _viewStates = MutableStateFlow(initialState)
    private val _viewActions = MutableSharedFlow<Action>(replay = 0)

    private var isFirstTimeInit: Boolean = true

    protected var viewState: State
        get() = _viewStates.value
        set(value) {
            _viewStates.value = value
        }

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
}