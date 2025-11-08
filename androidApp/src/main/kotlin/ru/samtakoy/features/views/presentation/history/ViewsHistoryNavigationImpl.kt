package ru.samtakoy.features.views.presentation.history

import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.features.views.presentation.history.vm.ViewsHistoryViewModel

internal class ViewsHistoryNavigationImpl : ViewsHistoryViewModel.Navigation {

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

    override fun onAction(action: ViewsHistoryViewModel.NavigationAction) {
        when (action) {
            is ViewsHistoryViewModel.NavigationAction.NavigateToQPackInfo -> {
                /*
                routerHolder?.navController?.navigate(
                    R.id.action_viewsHistoryFragment_to_cardsViewFragment,
                    QPackInfoFragment.buildBundle(action.qPackId)
                )*/
            }
        }
    }
}