package ru.samtakoy.presentation.qpacks.entry

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.navigation.presentation.RootFeatureEntry
import ru.samtakoy.presentation.qpacks.QPackInfoRoute
import ru.samtakoy.presentation.qpacks.screens.info.QPackInfoEntry

@Immutable
internal class QPackInfoEntryImpl : RootFeatureEntry {
    @Stable
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<QPackInfoRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<QPackInfoRoute>()
            QPackInfoEntry(
                qPackId = route.qPackId,
                rootNavController = rootNavController,
            )
        }
    }
}