package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.navigation.presentation.MainTabFeatureEntry
import ru.samtakoy.navigation.presentation.MainTabRoute
import ru.samtakoy.navigation.domain.model.TabRouteId
import ru.samtakoy.presentation.themes.list.ThemeListRoute

internal class ThemeListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = ThemeListRoute(themeId = 0L, themeTitle = null)

    override val tabId: TabRouteId = TabRouteId.ThemeList

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        currentNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<ThemeListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ThemeListRoute>()
            ThemeListEntry(
                rootNavController = rootNavController,
                navController = currentNavController,
                onMainNavigator = onMainNavigator,
                route = route
            )
        }
    }
}