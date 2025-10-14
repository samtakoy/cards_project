package ru.samtakoy.core.presentation.favorites.onboarding.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapper
import ru.samtakoy.domain.view.ViewHistoryInteractor

class FavoritesViewModelFactory @AssistedInject constructor(
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val favoritesButtonsMapper: FavoritesButtonsMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        require(modelClass == FavoritesViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return FavoritesViewModelImpl(
            viewHistoryInteractor = viewHistoryInteractor,
            favoritesInteractor = favoritesInteractor,
            favoritesButtonsMapper = favoritesButtonsMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(): FavoritesViewModelFactory
    }
}
