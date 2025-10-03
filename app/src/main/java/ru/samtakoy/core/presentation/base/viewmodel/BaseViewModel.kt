package ru.samtakoy.core.presentation.base.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AbstractViewModel {
    fun onViewCreated()
}

interface BaseViewModel<State: Any, Action, Event> : AbstractViewModel {
    fun getViewStateAsFlow(): StateFlow<State>
    fun getViewActionsAsFlow(): Flow<Action>
    fun onEvent(event: Event)
}