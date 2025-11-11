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

@Composable
internal fun ThemeListEntry(
    rootNavController: NavHostController,
    navController: NavHostController,
    onMainNavigator: () -> Unit,
    route: ThemeListRoute
) {
    ThemeListScreen(
        viewModel = koinViewModel<ThemeListViewModelImpl> {
            parametersOf(
                // themeId
                route.themeId,
                // themeTitle
                route.themeTitle
            )
        },
        onMainNavigator = onMainNavigator,
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
                    navController.navigate(ThemeListRoute(themeId = action.themeId, themeTitle = action.themeTitle))
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