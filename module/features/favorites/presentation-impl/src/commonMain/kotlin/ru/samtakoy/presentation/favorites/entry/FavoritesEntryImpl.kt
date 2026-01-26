package ru.samtakoy.presentation.favorites.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.favorites.FavoritesRoute
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.presentation.themes.entry.FavoritesEntry

// internal
class FavoritesEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = FavoritesRoute

    override val tabId: TabRouteId = TabRouteId.Favorites

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<FavoritesRoute> {
            FavoritesEntry()
        }
    }
}