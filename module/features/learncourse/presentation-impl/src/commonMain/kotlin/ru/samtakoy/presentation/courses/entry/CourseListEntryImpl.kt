package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId

// internal
class CourseListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = CourseListRoute

    override val tabId: TabRouteId = TabRouteId.CourseList

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<CourseListRoute> {
            CourseListEntry()
        }
    }
}