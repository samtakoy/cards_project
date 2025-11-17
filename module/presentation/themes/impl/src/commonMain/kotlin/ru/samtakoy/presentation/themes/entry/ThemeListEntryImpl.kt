package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.themes.list.ThemeListRoute

internal class ThemeListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = ThemeListRoute(themeId = 0L, themeTitle = null)

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<ThemeListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ThemeListRoute>()
            ThemeListEntry(
                rootNavController = rootNavController,
                navController = tabsNavController,
                onMainNavigator = onMainNavigator,
                route = route
            )
        }
    }
}