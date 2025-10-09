package ru.samtakoy.core.presentation.qpack.info.vm

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
import ru.samtakoy.features.learncourse.domain.NCoursesInteractor
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.features.qpack.domain.QPackInteractor
import ru.samtakoy.features.views.domain.ViewHistoryInteractor

internal class QPackInfoViewModelFactory @AssistedInject constructor(
    private val cardsInteractor: CardsInteractor,
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
            cardsInteractor = cardsInteractor,
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