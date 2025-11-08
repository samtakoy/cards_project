package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.qpacks.QPackListRoute

internal class QPackListEntryImpl : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = QPackListRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<QPackListRoute> {
            QPackListEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}