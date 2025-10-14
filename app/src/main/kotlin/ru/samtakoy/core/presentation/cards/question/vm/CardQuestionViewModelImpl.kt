package ru.samtakoy.core.presentation.cards.question.vm

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
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.Action
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.Event
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.State
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.common.utils.MyLog

class CardQuestionViewModelImpl(
    private val cardInteractor: CardInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewStateMapper: CardQuestionViewModelMapper,
    private val resources: Resources,
    scopeProvider: ScopeProvider,
    private val cardId: Long,
    viewMode: CardViewMode
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        question = AnnotatedString(""),
        isFavorite = null,
        prevCardButton = viewStateMapper.mapPrevCardButton(viewMode),
        viewAnswerButton = viewStateMapper.mapViewAnswerButton(viewMode),
        nextCardButton = viewStateMapper.mapNextCardButton(viewMode)
    )
), CardQuestionViewModel {

    init {
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.FavoriteChange -> onFavoriteChange(event.isChecked)
            Event.EditQuestionTextClick -> sendAction(Action.StartEditQuestionText)
            Event.NextCardClick -> sendAction(Action.ToNextCard)
            Event.PrevCardClick -> sendAction(Action.ToPrevCard)
            Event.ViewAnswerClick -> sendAction(Action.ViewAnswer)
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
                    question = card.question.asAnnotated(),
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