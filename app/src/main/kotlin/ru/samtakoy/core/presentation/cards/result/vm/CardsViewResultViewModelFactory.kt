package ru.samtakoy.core.presentation.cards.result.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.domain.view.ViewHistoryInteractor

class CardsViewResultViewModelFactory @AssistedInject constructor(
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val viewItemId: Long,
    @Assisted
    private val cardViewMode: CardViewMode
): AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == CardsViewResultViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return  CardsViewResultViewModelImpl(
            viewHistoryInteractor = viewHistoryInteractor,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider,
            viewItemId = viewItemId,
            cardViewMode = cardViewMode
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            viewItemId: Long,
            cardViewMode: CardViewMode
        ): CardsViewResultViewModelFactory
    }
}