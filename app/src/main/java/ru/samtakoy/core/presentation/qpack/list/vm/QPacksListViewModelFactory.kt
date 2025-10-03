package ru.samtakoy.core.presentation.qpack.list.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapper
import javax.inject.Inject

internal class QPacksListViewModelFactory @Inject constructor(
    private val cardsInteractor: CardsInteractor,
    private val itemsMapper: QPackListItemUiModelMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider
): AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == QPacksListViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return QPacksListViewModelImpl(
            cardsInteractor = cardsInteractor,
            itemsMapper = itemsMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider
        ) as T
    }
}