package ru.samtakoy.presentation.cards.screens.viewresult.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.serialize.ParcelableSchedule
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.Action
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.Event
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.State
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel

@Immutable
internal interface CardsViewResultViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val isLoading: Boolean,
        val content: Content?
    ) {
        @Immutable
        data class Content(
            // Results:
            val title: AnnotatedString,
            val viewedTitle: AnnotatedString,
            val errorsTitle: AnnotatedString?,
            val scheduleTitle: AnnotatedString?,
            val editScheduleButton: MyButtonUiModel?,
            val okButton: MyButtonUiModel,
        )
    }

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class ShowScheduleEditDialog(val schedule: Schedule) : Action
    }

    sealed interface NavigationAction : Action {
        object CloseScreen : NavigationAction
    }

    sealed interface Event {
        object ScheduleBtnClick : Event
        class NewScheduleDialogResult(val serializedSchedule: ParcelableSchedule?) : Event
        object OkBtnClick : Event
    }
}