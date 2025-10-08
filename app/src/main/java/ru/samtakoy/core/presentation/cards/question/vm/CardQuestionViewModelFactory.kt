package ru.samtakoy.core.presentation.cards.question.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.features.card.domain.CardsInteractor
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.core.presentation.cards.types.CardViewMode

internal class CardQuestionViewModelFactory @AssistedInject constructor(
    private val cardsInteractor: CardsInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewStateMapper: CardQuestionViewModelMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val cardId: Long,
    @Assisted
    private val viewMode: CardViewMode
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == CardQuestionViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return CardQuestionViewModelImpl(
            cardsInteractor = cardsInteractor,
            favoritesInteractor = favoritesInteractor,
            viewStateMapper = viewStateMapper,
            resources = resources,
            scopeProvider = scopeProvider,
            cardId = cardId,
            viewMode = viewMode
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            cardId: Long,
            viewMode: CardViewMode
        ): CardQuestionViewModelFactory
    }
}