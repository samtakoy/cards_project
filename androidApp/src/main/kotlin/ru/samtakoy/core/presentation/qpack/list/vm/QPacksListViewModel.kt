package ru.samtakoy.core.presentation.qpack.list.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.qpack.list.model.QPackListItemUiModel
import ru.samtakoy.core.presentation.qpack.list.model.QPackSortType
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.Action
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.Event
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.State

@Immutable
internal interface QPacksListViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val items: ImmutableList<QPackListItemUiModel>,
        val isFavoritesChecked: Boolean,
        val sortType: QPackSortType
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class SearchText(val text: String) : Action
    }

    sealed interface NavAction : Action {
        class ShowPackInfo(val qPackId: Long) : NavAction
    }

    sealed interface Event {
        class SearchTextChange(val text: String) : Event
        class FavoritesCheckBoxChange(val isChecked: Boolean) : Event
        class PackClick(val item: QPackListItemUiModel) : Event
        object SortByCreationDate : Event
        object SortByLastViewDate : Event
    }
}