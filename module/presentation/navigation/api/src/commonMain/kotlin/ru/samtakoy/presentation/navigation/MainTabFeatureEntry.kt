package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@Immutable
interface MainTabFeatureEntry {
    val route: MainTabRoute

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