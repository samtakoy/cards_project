package ru.samtakoy.core.presentation.favorites.onboarding

import ru.samtakoy.R
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.cards.CardsViewFragment
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.QPackSelectionFragment

internal class FavoritesNavigationImpl : FavoritesViewModel.Navigation {

    private var routerHolder: RouterHolder? = null

    override fun onAttach(routerHolder: RouterHolder) {
        this.routerHolder = routerHolder
    }

    override fun onDetach() {
        this.routerHolder = null
    }

    override fun navigateBack() {
        routerHolder?.navController?.popBackStack()
    }

    override fun onAction(action: FavoritesViewModel.NavigationAction) {
        when (action) {
            is FavoritesViewModel.NavigationAction.ViewCardsFromCourse -> {
                routerHolder?.navController?.navigate(
                    R.id.action_favoritesFragment_to_cardsViewFragment,
                    CardsViewFragment.buildBundle(
                        action.viewItemId,
                        CardViewMode.LEARNING
                    )
                )
            }
            FavoritesViewModel.NavigationAction.ViewQPacksWithFavs -> {
                routerHolder?.navController?.navigate(
                    R.id.action_favoritesFragment_to_qPackSelectionFragment,
                    QPackSelectionFragment.buildBundle()
                )
            }
        }
    }
}