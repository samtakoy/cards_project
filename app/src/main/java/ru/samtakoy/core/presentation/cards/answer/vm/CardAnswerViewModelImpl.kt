package ru.samtakoy.core.presentation.cards.answer.vm

import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Action
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Event
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.State
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.log.MyLog

internal class CardAnswerViewModelImpl(
    private val cardsInteractor: CardsInteractor,
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
        cardsInteractor.getCardAsFlow(cardId = cardId)
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