package ru.samtakoy.tabnavigation.presentation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId

@Immutable
interface MainTabFeatureEntry {
    val defaultRoute: MainTabRoute
    val tabId: TabRouteId

    /**
     * @param onMainNavigator колбэк открытия главного NavigationDrawer
     * */
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    )
}

fun MainTabFeatureEntry.getRouteName(): String {
    @OptIn(InternalSerializationApi::class)
    return defaultRoute::class.serializer().descriptor.serialName
}

fun String.getRouteWithoutArgs(): String {
    return substringBefore("/{")
}