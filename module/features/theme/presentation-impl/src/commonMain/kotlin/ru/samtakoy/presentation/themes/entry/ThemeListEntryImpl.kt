package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.presentation.themes.list.ThemeListRoute

internal class ThemeListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = ThemeListRoute(themeId = 0L, themeTitle = null)

    override val tabId: TabRouteId = TabRouteId.ThemeList

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<ThemeListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ThemeListRoute>()
            ThemeListEntry(
                rootNavController = rootNavController,
                route = route
            )
        }
    }
}