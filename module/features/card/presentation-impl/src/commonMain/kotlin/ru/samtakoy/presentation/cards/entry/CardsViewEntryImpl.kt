package ru.samtakoy.presentation.cards.entry

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.collections.immutable.toImmutableMap
import ru.samtakoy.presentation.cards.CardsViewParams
import ru.samtakoy.presentation.cards.CardsViewResultParams
import ru.samtakoy.presentation.cards.CardsViewResultRoute
import ru.samtakoy.presentation.cards.CardsViewRoute
import ru.samtakoy.navigation.presentation.RootFeatureEntry
import ru.samtakoy.navigation.presentation.base.navType
import kotlin.reflect.typeOf

@Immutable
internal class CardsViewEntryImpl : RootFeatureEntry {
    @Stable
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    ) {

        navGraphBuilder.composable<CardsViewRoute>(
            typeMap = cardsViewParamsTypeMap
        ) { backStackEntry ->
            val params = backStackEntry.toRoute<CardsViewRoute>().params
            CardsViewEntry(
                rootNavController = rootNavController,
                viewHistoryId = params.viewHistoryId,
                viewMode = params.viewMode
            )
        }
        navGraphBuilder.composable<CardsViewResultRoute>(
            typeMap = cardsViewResultParamsTypeMap
        ) { backStackEntry ->
            val params = backStackEntry.toRoute<CardsViewResultRoute>().params
            CardsViewResultEntry(
                rootNavController = rootNavController,
                viewHistoryId = params.viewHistoryId,
                viewMode = params.viewMode
            )
        }
    }

    companion object {
        private val cardsViewParamsTypeMap = mapOf(
            typeOf<CardsViewParams>() to navType<CardsViewParams>(false)
        ).toImmutableMap()

        private val cardsViewResultParamsTypeMap = mapOf(
            typeOf<CardsViewResultParams>() to navType<CardsViewResultParams>(false)
        ).toImmutableMap()
    }
}