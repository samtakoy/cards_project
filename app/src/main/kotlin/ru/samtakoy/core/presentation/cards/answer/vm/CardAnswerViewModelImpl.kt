package ru.samtakoy.core.presentation.cards.answer.vm

import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Action
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Event
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.State
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.common.utils.MyLog

internal class CardAnswerViewModelImpl(
    private val cardInteractor: CardInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewStateMapper: CardAnswerViewModelMapper,
    private val resources: Resources,
    scopeProvider: ScopeProvider,
    qPackId: Long,
    private val cardId: Long,
    viewMode: CardViewMode
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        answer = AnnotatedString(""),
        isFavorite = null,
        backButton = viewStateMapper.mapBackButton(viewMode),
        wrongButton = viewStateMapper.mapWrongButton(viewMode),
        nextCardButton = viewStateMapper.mapNextCardButton(viewMode)
    )
) {
    init {
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.FavoriteChange -> onFavoriteChange(event.isChecked)
            Event.BackToQuestionClick -> sendAction(Action.BackToQuestion)
            Event.EditAnswerTextClick -> sendAction(Action.StartAnswerTextEdit)
            Event.NextCardClick -> sendAction(Action.ToNextCard)
            Event.WrongAnswerClick -> sendAction(Action.WrongAnswer)
        }
    }

    private fun onFavoriteChange(isChecked: Boolean) {
        launchCatching(
            onError = ::onGetError
        ) {
            favoritesInteractor.setCardFavorite(cardId, if (isChecked) 1 else 0)
        }
    }

    private fun subscribeData() {
        cardInteractor.getCardAsFlow(cardId = cardId)
            .filterNotNull()
            .onEach { card ->
                viewState = viewState.copy(
                    answer = card.answer.asAnnotated(),
                    isFavorite = viewStateMapper.mapFavoriteItem(card.favorite > 0)
                )
            }
            .launchIn(mainScope)
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t))
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message))
        )
    }
}