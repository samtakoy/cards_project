package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.presentation.viewshistory.ViewsHistoryRoute

// internal
class ViewsHistoryEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = ViewsHistoryRoute

    override val tabId: TabRouteId = TabRouteId.ViewsHistory

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<ViewsHistoryRoute> {
            ViewsHistoryEntry()
        }
    }
}