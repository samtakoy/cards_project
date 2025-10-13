package ru.samtakoy.core.presentation.cards.result.vm

import androidx.compose.runtime.Immutable
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Action
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Event
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.State
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.serialize.ParcelableSchedule

@Immutable
interface CardsViewResultViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val isLearnView: Boolean,
        val viewedCardsCount: Int,
        val errorCardsCount: Int,
        val newSchedule: String
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class ShowScheduleEditDialog(val schedule: Schedule): Action
        class ResultOk(val newSchedule: Schedule) : Action
    }

    sealed interface Event {
        object ScheduleBtnClick : Event
        class NewScheduleDialogResult(val serializedSchedule: ParcelableSchedule?) : Event
        object OkBtnClick : Event
    }
}