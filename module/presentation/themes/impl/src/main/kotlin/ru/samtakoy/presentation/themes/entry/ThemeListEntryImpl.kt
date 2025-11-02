package ru.samtakoy.presentation.themes.entry

import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.themes.impl.R
import ru.samtakoy.presentation.themes.list.ThemeListRoute
import ru.samtakoy.presentation.utils.asA

internal class ThemeListEntryImpl(
    private val resources: Resources
) : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = ThemeListRoute(themeId = 0L, themeTitle = null)

    override val routeTitle: AnnotatedString
        get() = resources.getString(R.string.feature_themes_list_title).asA()

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<ThemeListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ThemeListRoute>()
            ThemeListEntry(
                rootNavController = rootNavController,
                navController = tabsNavController,
                onMainNavigator = onMainNavigator,
                route = route
            )
        }
    }
}