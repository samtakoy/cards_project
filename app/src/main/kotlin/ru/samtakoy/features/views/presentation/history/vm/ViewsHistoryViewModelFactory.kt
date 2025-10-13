package ru.samtakoy.features.views.presentation.history.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.features.views.presentation.history.mapper.ViewHistoryItemUiModelMapper

internal class ViewsHistoryViewModelFactory @AssistedInject constructor(
    private val viewsHistoryInteractor: ViewHistoryInteractor,
    private val itemsMapper: ViewHistoryItemUiModelMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        require(modelClass == ViewsHistoryViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return ViewsHistoryViewModelImpl(
            viewsHistoryInteractor = viewsHistoryInteractor,
            itemsMapper = itemsMapper,
            resources = resources,
            scopeProvider = scopeProvider
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(): ViewsHistoryViewModelFactory
    }
}