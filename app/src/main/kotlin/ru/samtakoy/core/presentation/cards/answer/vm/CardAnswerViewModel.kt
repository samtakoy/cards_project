package ru.samtakoy.core.presentation.cards.answer.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Action
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Event
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.State
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import ru.samtakoy.core.presentation.design_system.selectable_item.MySelectableItemModel

@Immutable
internal interface CardAnswerViewModel : BaseViewModel<State, Action, Event> {

    @Immutable
    data class State(
        val answer: AnnotatedString,
        val isFavorite: MySelectableItemModel?,
        val backButton: MyButtonModel?,
        val wrongButton: MyButtonModel?,
        val nextCardButton: MyButtonModel?
    )

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        object BackToQuestion : Action
        object WrongAnswer : Action
        object ToNextCard : Action
        object StartAnswerTextEdit : Action
    }

    sealed interface Event {
        class FavoriteChange(val isChecked: Boolean) : Event
        object BackToQuestionClick : Event
        object WrongAnswerClick : Event
        object NextCardClick : Event
        object EditAnswerTextClick : Event
    }
}