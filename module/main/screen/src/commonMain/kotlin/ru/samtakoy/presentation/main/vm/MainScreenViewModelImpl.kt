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
import ru.samtakoy.navigation.domain.model.TabRouteId

internal class MainScreenViewModelImpl(
    private val contentMapper: MainScreenContentMapper,
    scopeProvider: ScopeProvider,
    savedStateHandle: SavedStateHandle,
) : BaseViewModelImpl<MainScreenViewModel.State, MainScreenViewModel.Action, MainScreenViewModel.Event>(
    scopeProvider = scopeProvider,
    initialState = MainScreenViewModel.State(
        menuItems = emptyList<MainScreenViewModel.MenuItem>().toImmutableList(),
        selectedItemId = null
    )
), MainScreenViewModel {

    val selectedItemId: SavedStateValue<TabRouteId> = SavedStateValue(
        initialValueGetter = { TabRouteId.ThemeList },
        keyName = KEY_SELECTED_ITEM,
        savedStateHandle = savedStateHandle,
        serialize = { it.name },
        deserialize = {
            TabRouteId.valueOf(it)
        },
        saveScope = ioScope
    )

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

    override fun onEvent(event: MainScreenViewModel.Event) {
        when (event) {
            is MainScreenViewModel.Event.MenuItemClick -> onUIMenuItemClick(event.item)
            is MainScreenViewModel.Event.NavigationChangedExternally -> onUiNavigationChange(event.tabRouteId)
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
    }

    companion object {
        private const val KEY_SELECTED_ITEM = "KEY_SELECTED_ITEM"
    }
}