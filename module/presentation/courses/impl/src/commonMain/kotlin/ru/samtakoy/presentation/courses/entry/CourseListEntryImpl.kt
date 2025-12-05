package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.navigation.TabRouteId

// internal
class CourseListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = CourseListRoute

    override val tabId: TabRouteId = TabRouteId.CourseList

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        currentNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<CourseListRoute> {
            CourseListEntry(
                navController = currentNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}