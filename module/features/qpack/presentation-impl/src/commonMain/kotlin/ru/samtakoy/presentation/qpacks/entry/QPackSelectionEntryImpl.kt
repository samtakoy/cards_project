package ru.samtakoy.presentation.qpacks.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute

internal class QPackSelectionEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = QPackSelectionRoute

    override val tabId: TabRouteId = TabRouteId.QPackSelection

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<QPackSelectionRoute> {
            QPackSelectionEntry()
        }
    }
}