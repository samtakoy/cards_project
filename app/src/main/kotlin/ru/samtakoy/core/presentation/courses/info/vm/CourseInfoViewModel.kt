package ru.samtakoy.core.presentation.courses.info.vm

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.Action
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.Event
import ru.samtakoy.core.presentation.courses.info.vm.CourseInfoViewModel.State

@Immutable
interface CourseInfoViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val type: Type,
        val isLoading: Boolean
    ) {
        sealed interface Type {
            object Initialization : Type
            data class Data(
                val titleText: String,
                val cardsCountText: String,
                val statusString: String,
                val scheduleButtonText: String,
                val actionButtonText: String
            ) : Type
        }
    }

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        object RequestExtraordinaryRepeating : Action

    }

    sealed interface NavigationAction : Action {
        object Exit : NavigationAction
        class NavigateToCardsViewScreen(
            val viewHistoryItemId: Long,
            val viewMode: CardViewMode
        ) : Action
    }

    sealed interface Event {
        object DeleteCourseClick : Event
        object ActionButtonClick : Event
        object StartRepeatingExtraConfirm : Event
    }
}