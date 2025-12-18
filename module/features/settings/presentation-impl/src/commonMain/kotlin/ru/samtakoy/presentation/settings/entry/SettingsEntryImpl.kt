package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.navigation.TabRouteId
import ru.samtakoy.presentation.settings.SettingsRoute

internal class SettingsEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = SettingsRoute

    override val tabId: TabRouteId = TabRouteId.Settings

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        currentNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<SettingsRoute> {
            SettingsEntry(
                navController = currentNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}