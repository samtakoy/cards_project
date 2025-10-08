package ru.samtakoy.core.presentation.cards.vm

import androidx.compose.runtime.Immutable
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule

@Immutable
interface CardsViewViewModel : BaseViewModel<CardsViewViewModel.State, CardsViewViewModel.Action, CardsViewViewModel.Event> {

    @Immutable
    data class State(
        val type: Type,
        val viewMode: CardViewMode,
        val isLoading: Boolean
    ) {
        sealed interface Type {
            object Initialization : Type
            data class Error(val errorText: String) : Type
            data class Card(
                val qPackId: Long,
                val cardId: Long,
                val cardIndex: Int,
                val cardsCount: Int,
                val onAnswer: Boolean,
                val hasRevertButton: Boolean
            ) : Type
            data class Results(
                val viewItemId: Long
            ): Type
        }
    }

    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        class ShowEditTextDialog(
            val text: String,
            val question: Boolean
        ) : Action
    }

    sealed interface NavigationAction : Action {
        object CloseScreen : NavigationAction
    }

    sealed interface Event {
        object PrevCardClick : Event
        object ViewAnswerClick : Event
        object NextCardClick : Event
        object WrongAnswerClick : Event
        object BackToQuestionClick : Event
        class ScheduleEditResultOk(val newErrorCardsSchedule: Schedule) : Event
        object QuestionEditTextClick : Event
        object AnswerEditTextClick : Event
        class QuestionTextChanged(val newText: String) : Event
        class AnswerTextChanged(val newText: String) : Event
        object RevertClick : Event
    }
}