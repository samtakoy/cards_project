package ru.samtakoy.core.presentation.cards.question.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.Action
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.Event
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.State
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItemModel

@Immutable
interface CardQuestionViewModel : BaseViewModel<State, Action, Event> {
    @Immutable
    data class State(
        val question: AnnotatedString,
        val isFavorite: MySelectableItemModel?,
        val prevCardButton: MyButtonModel?,
        val viewAnswerButton: MyButtonModel?,
        val nextCardButton: MyButtonModel?,
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        object ToPrevCard : Action
        object ToNextCard : Action
        object ViewAnswer : Action
        object StartEditQuestionText : Action
    }

    sealed interface Event {
        class FavoriteChange(val isChecked: Boolean) : Event
        object PrevCardClick : Event
        object NextCardClick : Event
        object ViewAnswerClick : Event
        object EditQuestionTextClick : Event
    }
}