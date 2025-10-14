package ru.samtakoy.core.presentation.qpack.info.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractor

internal class QPackInfoViewModelFactory @AssistedInject constructor(
    private val cardInteractor: CardInteractor,
    private val qPackInteractor: QPackInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val coursesInteractor: NCoursesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    private val cardsMapper: FastCardUiModelMapper,
    @Assisted
    private val qPackId: Long
): AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == QPackInfoViewModelImpl::class.java)
        return QPackInfoViewModelImpl(
            cardInteractor = cardInteractor,
            qPackInteractor = qPackInteractor,
            favoritesInteractor = favoritesInteractor,
            coursesInteractor = coursesInteractor,
            viewHistoryInteractor = viewHistoryInteractor,
            resources = resources,
            cardsMapper = cardsMapper,
            scopeProvider = scopeProvider,
            qPackId = qPackId
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(qPackId: Long): QPackInfoViewModelFactory
    }
}