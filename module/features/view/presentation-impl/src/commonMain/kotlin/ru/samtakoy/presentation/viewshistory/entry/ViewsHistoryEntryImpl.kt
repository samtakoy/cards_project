package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.navigation.TabRouteId
import ru.samtakoy.presentation.viewshistory.ViewsHistoryRoute

// internal
class ViewsHistoryEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = ViewsHistoryRoute

    override val tabId: TabRouteId = TabRouteId.ViewsHistory

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        currentNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<ViewsHistoryRoute> {
            ViewsHistoryEntry(
                navController = currentNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}