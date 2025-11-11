package ru.samtakoy.presentation.cards.screens.view.vm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel

@Immutable
internal interface CardsViewViewModel :
    BaseViewModel<CardsViewViewModel.State, CardsViewViewModel.Action, CardsViewViewModel.Event> {

    @Immutable
    data class State(
        val type: Type,
        val isLoading: Boolean,
        val cardItems: ImmutableList<CardState>,
        val questionButtons: ImmutableList<MyButtonUiModel>,
        val answerButtons: ImmutableList<MyButtonUiModel>,
    ) {
        @Immutable
        sealed interface Type {
            object Initialization : Type
            data class Error(val errorText: String) : Type
            /**
             * @param cardIndex индекс айтема (вопрос либо ответ) карточки в [cardItems]
             * */
            data class Card(
                val cardsCountTitle: AnnotatedString,
                val cardIndex: Int,
            ) : Type
        }
    }

    /** Отображение одной карточки - вопроса, либо ответа */
    @Immutable
    data class CardState(
        val id: Long,
        val isQuestion: Boolean,
        val content: Content?
    ) {
        data class Content(
            val isFavorite: Boolean,
            val text: AnnotatedString,
            val hasRevertButton: Boolean
        )
    }

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class ShowEditTextDialog(
            val text: String,
            val question: Boolean
        ) : Action
    }

    sealed interface NavigationAction : Action {
        class OpenResults(
            val viewHistoryItemId: Long,
            val cardViewMode: CardViewMode
        ) : NavigationAction
        object CloseScreen : NavigationAction
    }

    sealed interface Event {
        class ButtonClick(val id: UiId) : Event
        object FavoriteClick : Event
        object QuestionEditTextClick : Event
        object AnswerEditTextClick : Event
        class QuestionTextChanged(val newText: String) : Event
        class AnswerTextChanged(val newText: String) : Event
        object RevertClick : Event
    }
}