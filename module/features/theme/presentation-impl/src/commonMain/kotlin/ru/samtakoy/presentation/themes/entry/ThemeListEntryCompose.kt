package ru.samtakoy.presentation.themes.entry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.presentation.qpacks.QPackInfoRoute
import ru.samtakoy.presentation.themes.list.ThemeListRoute
import ru.samtakoy.presentation.themes.list.ThemeListScreen
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModelImpl
import ru.samtakoy.tabnavigation.presentation.navigator.LocalTabNavigator

@Composable
internal fun ThemeListEntry(
    rootNavController: NavHostController,
    route: ThemeListRoute
) {
    val tabNavigator = LocalTabNavigator.current
    ThemeListScreen(
        viewModel = koinViewModel<ThemeListViewModelImpl> {
            parametersOf(
                // themeId
                route.themeId,
                // themeTitle
                route.themeTitle
            )
        },
        onMainNavigator = { tabNavigator.showMainDrawer() },
        onNavigationAction = { action ->
            when (action) {
                is ThemeListViewModel.NavigationAction.NavigateToBatchExportDirDialog,
                ThemeListViewModel.NavigationAction.NavigateToBatchExportToEmailDialog,
                is ThemeListViewModel.NavigationAction.NavigateToBatchImportFromDirDialog,
                is ThemeListViewModel.NavigationAction.NavigateToImportPackDialog,
                ThemeListViewModel.NavigationAction.NavigateToLog,
                ThemeListViewModel.NavigationAction.NavigateToOnlineImport,
                ThemeListViewModel.NavigationAction.NavigateToSettings -> Unit // TODO()
                is ThemeListViewModel.NavigationAction.NavigateToTheme -> {
                    tabNavigator.putNextInStack(
                        ThemeListRoute(themeId = action.themeId, themeTitle = action.themeTitle)
                    )
                }
                is ThemeListViewModel.NavigationAction.NavigateToQPack -> {
                    rootNavController.navigate(QPackInfoRoute(qPackId = action.qPackId))
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    )
}