package ru.samtakoy.features.views.presentation.history.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.features.views.presentation.history.components.OneViewHistoryItemModel
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.Action
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.Event
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel.State

@Immutable
interface ViewsHistoryViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val type: Type,
        val items: ImmutableList<OneViewHistoryItemModel>
    ) {
        enum class Type {
            Loading,
            Data
        }
    }

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
    }

    sealed interface NavigationAction : Action {
        class NavigateToQPackInfo(val qPackId: Long) : NavigationAction
    }

    sealed interface Event {
        class ItemClick(val item: OneViewHistoryItemModel) : Event
    }

    @Immutable
    interface Navigation {
        fun onAttach(routerHolder: RouterHolder)
        fun onDetach()
        fun navigateBack()
        fun onAction(action: NavigationAction)
    }
}