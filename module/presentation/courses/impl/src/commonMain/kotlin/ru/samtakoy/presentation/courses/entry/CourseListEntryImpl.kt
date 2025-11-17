package ru.samtakoy.presentation.themes.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute

// internal
class CourseListEntryImpl : MainTabFeatureEntry {

    override val defaultRoute: MainTabRoute
        get() = CourseListRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<CourseListRoute> {
            CourseListEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}