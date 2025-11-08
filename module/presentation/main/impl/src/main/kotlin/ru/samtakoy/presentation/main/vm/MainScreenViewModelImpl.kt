package ru.samtakoy.presentation.main.vm

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdThemeListRoute

internal class MainScreenViewModelImpl(
    private val contentMapper: MainScreenContentMapper,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<MainScreenViewModel.State, MainScreenViewModel.Action, MainScreenViewModel.Event>(
    scopeProvider = scopeProvider,
    initialState = MainScreenViewModel.State(
        menuItems = emptyList<MainScreenViewModel.MenuItem>().toImmutableList(),
        selectedItemId = IdThemeListRoute
    )
), MainScreenViewModel {

    init {
        launchCatching {
            viewState = viewState.copy(
                menuItems = contentMapper.mapMenuItems().toImmutableList(),
            )
        }
    }

    override fun onEvent(event: MainScreenViewModel.Event) {
        when (event) {
            is MainScreenViewModel.Event.MenuItemClick -> onMenuItemClick(event.item)
        }
    }

    private fun onMenuItemClick(item: MainScreenViewModel.MenuItem) {
        if (item.id == viewState.selectedItemId) return
        viewState = viewState.copy(
            selectedItemId = item.id
        )
    }
}