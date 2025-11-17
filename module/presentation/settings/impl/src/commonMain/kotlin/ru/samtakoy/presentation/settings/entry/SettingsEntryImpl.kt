package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.settings.SettingsRoute

internal class SettingsEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = SettingsRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<SettingsRoute> {
            SettingsEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}