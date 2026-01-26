package ru.samtakoy.presentation.main.vm

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.Action
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.Event
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.State
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigator

@Immutable
internal interface MainScreenViewModel : BaseViewModel<State, Action, Event> {

    @get:Stable
    val tabNavigator: TabNavigator

    @Immutable
    data class State(
        val menuItems: ImmutableList<MenuItem>,
        val selectedItemId: TabRouteId?
    )

    sealed interface Action {
        object ShowMainDrawer : Action
        class AddTabFlowScreen(val route: MainTabRoute) : Action
    }
    sealed interface NavigationAction : Action

    sealed interface Event {
        class MenuItemClick(val item: MenuItem) : Event
        class NavigationChangedExternally(val tabRouteId: TabRouteId) : Event
    }

    @Immutable
    data class MenuItem(
        val id: UiId,
        val title: AnnotatedString
    )
}