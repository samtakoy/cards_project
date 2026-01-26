package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.presentation.qpacks.QPackListRoute
import ru.samtakoy.presentation.qpacks.screens.list.QPackListEntry

internal class QPackListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = QPackListRoute

    override val tabId: TabRouteId = TabRouteId.QPackList

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<QPackListRoute> {
            QPackListEntry(
                rootNavController = rootNavController
            )
        }
    }
}