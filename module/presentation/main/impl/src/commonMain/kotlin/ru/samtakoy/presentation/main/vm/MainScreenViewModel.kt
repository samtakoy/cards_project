package ru.samtakoy.presentation.main.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.Action
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.Event
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.State

@Immutable
internal interface MainScreenViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val menuItems: ImmutableList<MenuItem>,
        val selectedItemId: UiId?
    )

    sealed interface Action {
        class OpenTabScreen(val selectedItemId: AnyUiId) : Action
    }
    sealed interface NavigationAction : Action

    sealed interface Event {
        class MenuItemClick(val item: MenuItem) : Event
    }

    @Immutable
    data class MenuItem(
        val id: UiId,
        val title: AnnotatedString
    )
}