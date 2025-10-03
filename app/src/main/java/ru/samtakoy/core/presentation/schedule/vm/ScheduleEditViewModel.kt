package ru.samtakoy.core.presentation.schedule.vm

import androidx.compose.runtime.Immutable
import ru.samtakoy.core.data.local.database.room.entities.elements.ScheduleTimeUnit
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.Action
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.Event
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.State

@Immutable
internal interface ScheduleEditViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val schedule: String,
        val scheduleCurItemButtonText: String
    )

    sealed interface Action {
        class CloseWithResult(val serializedSchedule: String) : Action
    }

    sealed interface Event {
        object ClearScheduleClick : Event
        object ClearScheduleCurItemClick : Event
        object AddScheduleCurItem : Event
        class ScheduleTimeUnitSelect(val unit: ScheduleTimeUnit) : Event
        object ConfirmResultClick : Event
    }
}