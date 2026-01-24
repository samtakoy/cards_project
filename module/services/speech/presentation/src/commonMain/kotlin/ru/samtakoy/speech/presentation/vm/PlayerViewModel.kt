package ru.samtakoy.speech.presentation.vm

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.core.design_system.player.MyPlayerUiModel
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.Action
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.Event
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.State

@Immutable
interface PlayerViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val player: MyPlayerUiModel
    )
    sealed interface Action
    sealed interface Event {
        class ControlClick(val controlId: UiId) : Event
    }
}