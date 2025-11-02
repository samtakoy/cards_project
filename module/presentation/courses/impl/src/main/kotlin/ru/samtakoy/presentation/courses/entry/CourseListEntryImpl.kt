package ru.samtakoy.presentation.themes.entry

import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.courses.impl.R
import ru.samtakoy.presentation.utils.asA

// internal
class CourseListEntryImpl(
    private val resources: Resources
) : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = CourseListRoute

    override val routeTitle: AnnotatedString
        get() = resources.getString(R.string.feature_courses_title).asA()

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