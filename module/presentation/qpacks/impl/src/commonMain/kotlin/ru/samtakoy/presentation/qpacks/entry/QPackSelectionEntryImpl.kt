package ru.samtakoy.presentation.qpacks.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.navigation.TabRouteId
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute

internal class QPackSelectionEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = QPackSelectionRoute

    override val tabId: TabRouteId = TabRouteId.QPackSelection

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        currentNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<QPackSelectionRoute> {
            QPackSelectionEntry(
                navController = currentNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}