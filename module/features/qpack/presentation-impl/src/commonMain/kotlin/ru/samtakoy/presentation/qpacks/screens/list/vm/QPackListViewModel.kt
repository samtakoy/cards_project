package ru.samtakoy.presentation.qpacks.screens.list.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemUiModel
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.Action
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.Event
import ru.samtakoy.presentation.qpacks.screens.list.vm.QPackListViewModel.State

@Immutable
internal interface QPackListViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val isLoading: Boolean,
        val title: AnnotatedString,
        val items: ImmutableList<QPackListItemUiModel>,
        val scrollPoint: LastScrollPoint,
        val isFavoritesChecked: Boolean,
        val sortButton: MyButtonUiModel?
    )

    val searchText: androidx.compose.runtime.MutableState<String>

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
    }

    sealed interface NavigationAction : Action {
        class ShowPackInfo(val qPackId: Long) : NavigationAction
        object OpenMainNavigator: NavigationAction
    }

    sealed interface Event {
        class SearchTextChange(val text: String) : Event
        class FavoritesCheckBoxChange(val isChecked: Boolean) : Event
        class PackClick(val itemId: LongUiId) : Event
        class SortButtonClick(
            val buttonId: UiId,
            val scrollIndex: Int,
            val scrollOffset: Int
        ) : Event
        object MainNavigatorIconClick : Event
    }

    @Immutable
    data class LastScrollPoint(
        val id: Long,
        val scrollIndexBeforeOp: Int,
        val scrollOffsetBeforeOp: Int
    )
}