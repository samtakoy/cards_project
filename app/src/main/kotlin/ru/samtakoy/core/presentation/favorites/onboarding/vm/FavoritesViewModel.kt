package ru.samtakoy.core.presentation.favorites.onboarding.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.design_system.base.model.UiId
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel.Action
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel.Event
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel.State

@Immutable
interface FavoritesViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val isLoaderVisible: Boolean,
        val buttons: ImmutableList<MyButtonModel>
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
    }

    sealed interface NavigationAction : Action {
        class ViewCardsFromCourse(
            val viewItemId: Long
        ) : NavigationAction
        object ViewQPacksWithFavs : NavigationAction
    }

    sealed interface Event {
        class ButtonClick(val id: UiId) : Event
    }

    @Immutable
    interface Navigation {
        fun onAttach(routerHolder: RouterHolder)
        fun onDetach()
        fun navigateBack()
        fun onAction(action: NavigationAction)
    }
}