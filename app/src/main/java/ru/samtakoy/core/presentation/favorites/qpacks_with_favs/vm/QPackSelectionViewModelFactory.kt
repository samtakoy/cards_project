package ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.mapper.QPacksWithFavsItemsMapper
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import javax.inject.Inject

class QPackSelectionViewModelFactory @Inject constructor(
    private val cardsInteractor: CardsInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val itemsMapper: QPacksWithFavsItemsMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        require(modelClass == QPackSelectionViewModelImpl::class.java)
        return QPackSelectionViewModelImpl(
            cardsInteractor = cardsInteractor,
            favoritesInteractor = favoritesInteractor,
            viewHistoryInteractor = viewHistoryInteractor,
            itemsMapper = itemsMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider
        ) as T
    }
}