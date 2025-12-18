package ru.samtakoy.presentation.qpacks.screens.info.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.qpacks.screens.fastlist.model.FastCardUiModel
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.Action
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.State
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel

@Immutable
internal interface QPackInfoViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val isLoading: Boolean,
        val title: AnnotatedString,
        val toolbarMenu: DropDownMenuUiModel,
        val cardsCountText: AnnotatedString,
        val isFavoriteChecked: Boolean,
        val buttons: ImmutableList<MyButtonUiModel>,
        val fastCards: CardsState
    ) {
        sealed interface CardsState {
            object NotInit : CardsState
            data class Data(
                val cards: ImmutableList<FastCardUiModel>
            ) : CardsState
        }
    }

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class RequestNewCourseCreation(val title: String) : Action
        class RequestsSelectCourseToAdd(val qPackId: Long) : Action
        class ShowChoiceDialog(val dialogModel: MyChoiceDialogUiModel) : Action
        object OpenCardsInBottomList : Action
    }

    sealed interface NavigationAction : Action {
        object CloseScreen : NavigationAction
        class ShowCourseScreen(val courseId: Long) : NavigationAction
        class NavigateToPackCourses(val qPackId: Long) : NavigationAction
        class NavigateToCardsView(val viewItemId: Long) : NavigationAction

    }

    sealed interface Event {
        class NewCourseCommit(val courseTitle: String) : Event
        class ButtonClick(val btnId: UiId) : Event
        class AddCardsToCourseCommit(val courseId: Long) : Event
        class ChoiceDialoButtonClick(val dialogId: UiId?, val buttonId: UiId, val itemId: UiId) : Event
        object CardsFastView : Event
        class ToolbarMenuItemClick(val menuItemId: UiId) : Event
        class FavoriteChange(val wasChecked: Boolean) : Event
    }
}