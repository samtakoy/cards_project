package ru.samtakoy.core.presentation.cards.answer.vm

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
import javax.inject.Named

internal class CardAnswerViewModelFactory @AssistedInject constructor(
    private val cardsInteractor: CardsInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewStateMapper: CardAnswerViewModelMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted("qPackId")
    private val qPackId: Long,
    @Assisted("cardId")
    private val cardId: Long,
    @Assisted("viewMode")
    private val viewMode: CardViewMode
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == CardAnswerViewModelImpl::class.java)
        return CardAnswerViewModelImpl(
            cardsInteractor = cardsInteractor,
            favoritesInteractor = favoritesInteractor,
            viewStateMapper = viewStateMapper,
            resources = resources,
            scopeProvider = scopeProvider,
            qPackId = qPackId,
            cardId = cardId,
            viewMode = viewMode,
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(value = "qPackId")
            qPackId: Long,
            @Assisted(value = "cardId")
            cardId: Long,
            @Assisted(value = "viewMode")
            viewMode: CardViewMode
        ): CardAnswerViewModelFactory
    }
}