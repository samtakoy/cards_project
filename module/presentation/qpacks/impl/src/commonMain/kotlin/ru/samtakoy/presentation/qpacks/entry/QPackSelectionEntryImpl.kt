package ru.samtakoy.presentation.qpacks.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute

internal class QPackSelectionEntryImpl : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = QPackSelectionRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<QPackSelectionRoute> {
            QPackSelectionEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}