package ru.samtakoy.oldlegacy.core.presentation.favorites.qpacks_with_favs

import ru.samtakoy.oldlegacy.core.presentation.RouterHolder
import ru.samtakoy.oldlegacy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel

class QPacksWithFavsNavigationImpl : QPackSelectionViewModel.Navigation {

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

    override fun onAction(action: QPackSelectionViewModel.NavigationAction) {
        when (action) {
            is QPackSelectionViewModel.NavigationAction.ViewCardsFromCourse -> {
                /*
                routerHolder?.navController?.navigate(
                    R.id.action_qPackSelectionFragment_to_cardsViewFragment,
                    CardsViewFragment.buildBundle(
                        action.viewItemId,
                        CardViewMode.LEARNING
                    )
                )*/
            }
        }
    }
}