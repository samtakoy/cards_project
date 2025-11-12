package ru.samtakoy.presentation.cards.entry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.samtakoy.presentation.cards.CardsViewParams
import ru.samtakoy.presentation.cards.CardsViewResultParams
import ru.samtakoy.presentation.cards.CardsViewResultRoute
import ru.samtakoy.presentation.cards.CardsViewRoute
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.navigation.RootFeatureEntry
import ru.samtakoy.presentation.navigation.base.navType
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class CardsViewEntryImpl : RootFeatureEntry {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {
        navGraphBuilder.composable<CardsViewRoute>(
            typeMap = mapOf(
                typeOf<CardsViewParams>() to navType<CardsViewParams>(false)
            )
        ) { backStackEntry ->
            val params = backStackEntry.toRoute<CardsViewRoute>().params
            CardsViewEntry(
                rootNavController = rootNavController,
                viewHistoryId = params.viewHistoryId,
                viewMode = params.viewMode
            )
        }
        navGraphBuilder.composable<CardsViewResultRoute>(
            typeMap = mapOf(
                typeOf<CardsViewResultParams>() to navType<CardsViewResultParams>(false)
            )
        ) { backStackEntry ->
            val params = backStackEntry.toRoute<CardsViewResultRoute>().params
            CardsViewResultEntry(
                rootNavController = rootNavController,
                viewHistoryId = params.viewHistoryId,
                viewMode = params.viewMode
            )
        }
    }
}