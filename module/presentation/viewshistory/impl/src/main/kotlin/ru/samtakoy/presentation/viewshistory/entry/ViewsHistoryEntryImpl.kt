package ru.samtakoy.presentation.themes.entry

import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.viewshistory.impl.R
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.viewshistory.ViewsHistoryRoute

// internal
class ViewsHistoryEntryImpl(
    private val resources: Resources
) : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = ViewsHistoryRoute

    override val routeTitle: AnnotatedString
        get() = resources.getString(R.string.feature_views_history_title).asA()

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<ViewsHistoryRoute> {
            ViewsHistoryEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}