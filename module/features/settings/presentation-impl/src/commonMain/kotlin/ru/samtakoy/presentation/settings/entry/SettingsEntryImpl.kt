package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.presentation.settings.SettingsRoute

internal class SettingsEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = SettingsRoute

    override val tabId: TabRouteId = TabRouteId.Settings

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<SettingsRoute> {
            SettingsEntry()
        }
    }
}