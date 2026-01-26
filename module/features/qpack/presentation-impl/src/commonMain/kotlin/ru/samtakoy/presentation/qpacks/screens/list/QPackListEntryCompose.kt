package ru.samtakoy.presentation.qpacks.screens.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import ru.samtakoy.presentation.qpacks.QPackInfoRoute
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.NavigationAction
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModelImpl
import ru.samtakoy.tabnavigation.presentation.navigator.LocalTabNavigator

@Composable
internal fun QPackListEntry(
    rootNavController: NavHostController
) {
    val tabNavigator = LocalTabNavigator.current
    QPackListScreen(
        viewModel = koinViewModel<QPackListViewModelImpl>(),
        onNavigationAction = { action ->
            when (action) {
                is NavigationAction.ShowPackInfo -> rootNavController.navigate(QPackInfoRoute(qPackId = action.qPackId))
                NavigationAction.OpenMainNavigator -> { tabNavigator.showMainDrawer() }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    )
}