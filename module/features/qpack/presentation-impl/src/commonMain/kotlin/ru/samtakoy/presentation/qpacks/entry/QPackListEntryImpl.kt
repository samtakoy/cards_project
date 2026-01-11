package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.navigation.TabRouteId
import ru.samtakoy.presentation.qpacks.QPackListRoute
import ru.samtakoy.presentation.qpacks.screens.list.QPackListEntry

internal class QPackListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = QPackListRoute

    override val tabId: TabRouteId = TabRouteId.QPackList

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        currentNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<QPackListRoute> {
            QPackListEntry(
                rootNavController = rootNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}