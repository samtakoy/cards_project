package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.navigation.presentation.MainTabFeatureEntry
import ru.samtakoy.navigation.presentation.MainTabRoute
import ru.samtakoy.navigation.domain.model.TabRouteId
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