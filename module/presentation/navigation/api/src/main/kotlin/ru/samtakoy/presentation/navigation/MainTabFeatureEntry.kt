package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@Immutable
interface MainTabFeatureEntry {
    val route: MainTabRoute
    @Deprecated("не используется")
    val routeTitle: AnnotatedString

    /**
     * @param onMainNavigator колбэк открытия главного NavigationDrawer
     * */
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    )
}