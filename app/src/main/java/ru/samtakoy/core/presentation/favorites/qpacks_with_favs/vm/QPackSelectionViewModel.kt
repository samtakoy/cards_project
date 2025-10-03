package ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel.NavigationAction
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.Action
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.Event
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel.State

@Immutable
interface QPackSelectionViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val isLoaderVisible: Boolean,
        val items: ImmutableList<MySelectableItemModel>,
        val actionButton: MyButtonModel
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
    }

    sealed interface NavigationAction : Action {
        class ViewCardsFromCourse(val viewItemId: Long) : NavigationAction
    }

    sealed interface Event {
        class ItemClick(val item: MySelectableItemModel) : Event
        object ActionButtonClick : Event
    }

    @Immutable
    interface Navigation {
        fun onAttach(routerHolder: RouterHolder)
        fun onDetach()
        fun navigateBack()
        fun onAction(action: NavigationAction)
    }
}