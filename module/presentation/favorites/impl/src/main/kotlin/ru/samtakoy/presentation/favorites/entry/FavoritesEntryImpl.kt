package ru.samtakoy.presentation.favorites.entry

import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.favorites.FavoritesRoute
import ru.samtakoy.presentation.favorites.impl.R
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.themes.entry.FavoritesEntry
import ru.samtakoy.presentation.utils.asA

// internal
class FavoritesEntryImpl(
    private val resources: Resources
) : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = FavoritesRoute

    override val routeTitle: AnnotatedString
        get() = resources.getString(R.string.feature_favorites_title).asA()

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<FavoritesRoute> {
            FavoritesEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}