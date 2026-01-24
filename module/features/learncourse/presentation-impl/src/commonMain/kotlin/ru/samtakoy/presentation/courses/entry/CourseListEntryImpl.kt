package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.navigation.presentation.MainTabFeatureEntry
import ru.samtakoy.navigation.presentation.MainTabRoute
import ru.samtakoy.navigation.domain.model.TabRouteId

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