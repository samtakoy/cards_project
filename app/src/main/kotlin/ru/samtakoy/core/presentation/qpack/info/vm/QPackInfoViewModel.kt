package ru.samtakoy.core.presentation.qpack.info.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.qpack.info.FastCardUiModel
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Action
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.State

@Immutable
internal interface QPackInfoViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val isLoading: Boolean,
        val title: AnnotatedString,
        val cardsCountText: AnnotatedString,
        val isFavoriteChecked: Boolean,
        val uncompletedButton: AnnotatedString?,
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
        object ShowLearnCourseCardsViewingType : Action
        object OpenCardsInBottomList : Action
    }

    sealed interface NavigationAction : Action {
        object CloseScreen : NavigationAction
        class ShowCourseScreen(val courseId: Long) : NavigationAction
        class NavigateToPackCourses(val qPackId: Long) : NavigationAction
        class NavigateToCardsView(val viewItemId: Long) : NavigationAction

    }

    sealed interface Event {
        object DeletePack : Event
        class NewCourseCommit(val courseTitle: String) : Event
        object ShowPackCourses : Event
        object AddToNewCourse : Event
        object AddToExistsCourse : Event
        class AddCardsToCourseCommit(val courseId: Long) : Event
        object ViewPackCards : Event
        object ViewUncompletedClick : Event
        object ViewPackCardsRandomly : Event
        object ViewPackCardsOrdered : Event
        object ViewPackCardsInList : Event
        object CardsFastView : Event
        object AddFakeCard : Event
        class FavoriteChange(val isChecked: Boolean) : Event
    }
}