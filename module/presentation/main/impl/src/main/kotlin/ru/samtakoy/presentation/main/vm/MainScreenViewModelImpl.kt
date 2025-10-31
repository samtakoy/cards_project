package ru.samtakoy.presentation.main.vm

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdThemeListRoute
import ru.samtakoy.presentation.themes.list.ThemeListRoute

internal class MainScreenViewModelImpl(
    private val contentMapper: MainScreenContentMapper,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<MainScreenViewModel.State, MainScreenViewModel.Action, MainScreenViewModel.Event>(
    scopeProvider = scopeProvider,
    initialState = MainScreenViewModel.State(
        menuItems = contentMapper.mapMenuItems().toImmutableList(),
        selectedItemId = IdThemeListRoute
    )
), MainScreenViewModel {

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