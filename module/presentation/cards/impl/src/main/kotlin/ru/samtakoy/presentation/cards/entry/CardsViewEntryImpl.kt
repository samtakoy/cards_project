package ru.samtakoy.presentation.cards.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.presentation.cards.CardsViewResultRoute
import ru.samtakoy.presentation.cards.CardsViewRoute
import ru.samtakoy.presentation.navigation.RootFeatureEntry

class CardsViewEntryImpl : RootFeatureEntry {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<CardsViewRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CardsViewRoute>()
            CardsViewEntry(
                rootNavController = rootNavController,
                viewHistoryId = route.viewHistoryId,
                viewMode = route.viewMode
            )
        }
        navGraphBuilder.composable<CardsViewResultRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CardsViewResultRoute>()
            CardsViewResultEntry(
                rootNavController = rootNavController,
                viewHistoryId = route.viewHistoryId,
                viewMode = route.viewMode
            )
        }
    }
}