package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import kotlin.reflect.KType

@Immutable
interface MainTabFeatureEntry {
    val route: MainTabRoute

    fun getTypeMap(): Map<KType, @JvmSuppressWildcards NavType<*>>? = null

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