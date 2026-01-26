package ru.samtakoy.presentation.main.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.Action
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.Event
import ru.samtakoy.presentation.main.vm.MainScreenViewModel.State
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigator
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigatorEvent
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigatorHost

internal class MainScreenViewModelImpl(
    private val contentMapper: MainScreenContentMapper,
    scopeProvider: ScopeProvider,
    savedStateHandle: SavedStateHandle,
    private val tabNavigatorHost: TabNavigatorHost
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        menuItems = emptyList<MainScreenViewModel.MenuItem>().toImmutableList(),
        selectedItemId = null
    )
), MainScreenViewModel {

    private val selectedItemId: SavedStateValue<TabRouteId> = SavedStateValue(
        initialValueGetter = { TabRouteId.ThemeList },
        keyName = KEY_SELECTED_ITEM,
        savedStateHandle = savedStateHandle,
        serialize = { it.name },
        deserialize = {
            TabRouteId.valueOf(it)
        },
        saveScope = ioScope
    )

    override val tabNavigator: TabNavigator
        get() = tabNavigatorHost.getTabNavigator()

    init {
        launchCatching {
            updateState { state ->
                state.copy(
                    menuItems = contentMapper.mapMenuItems().toImmutableList(),
                )
            }
        }
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.MenuItemClick -> onUIMenuItemClick(event.item)
            is Event.NavigationChangedExternally -> onUiNavigationChange(event.tabRouteId)
        }
    }

    private fun onTabNavigationEvent(event: TabNavigatorEvent) {
        when (event) {
            TabNavigatorEvent.ShowMainDrawer -> {
                sendAction(Action.ShowMainDrawer)
            }
            is TabNavigatorEvent.PutNextScreenInStack -> {
                sendAction(Action.AddTabFlowScreen(event.route))
            }
        }
    }

    private fun onUiNavigationChange(tabRouteId: TabRouteId) {
        selectedItemId.value = tabRouteId
    }

    private fun onUIMenuItemClick(item: MainScreenViewModel.MenuItem) {
        val clickedId = item.id as? TabRouteId ?: return
        selectedItemId.value = clickedId
    }

    private fun subscribeData() {
        selectedItemId.asFlow()
            .distinctUntilChanged()
            .onEach {
                updateState { state -> state.copy(selectedItemId = it) }
            }
            .launchIn(mainScope)
        tabNavigatorHost.getNavigationEventsAsFlow()
            .onEach {
                onTabNavigationEvent(it)
            }
            .launchIn(mainScope)
    }

    companion object {
        private const val KEY_SELECTED_ITEM = "KEY_SELECTED_ITEM"
    }
}