package ru.samtakoy.presentation.qpacks.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.presentation.navigation.RootFeatureEntry
import ru.samtakoy.presentation.qpacks.QPackInfoRoute
import ru.samtakoy.presentation.qpacks.screens.info.QPackInfoEntry

class QPackInfoEntryImpl : RootFeatureEntry {
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