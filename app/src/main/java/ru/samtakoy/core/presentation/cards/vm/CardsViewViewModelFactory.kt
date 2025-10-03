package ru.samtakoy.core.presentation.cards.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.CoursesPlanner
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import ru.samtakoy.features.views.domain.ViewHistoryProgressUseCase

internal class CardsViewViewModelFactory @AssistedInject constructor(
    private var cardsInteractor: CardsInteractor,
    private var viewHistoryInteractor: ViewHistoryInteractor,
    private val viewHistoryProgressUseCase: ViewHistoryProgressUseCase,
    private var coursesPlanner: CoursesPlanner,
    private val viewStateMapper: CardsViewViewStateMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val viewHistoryItemId: Long,
    @Assisted
    private val viewMode: CardViewMode
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == CardsViewViewModelImpl::class.java)
        return CardsViewViewModelImpl(
            cardsInteractor,
            viewHistoryInteractor,
            viewHistoryProgressUseCase,
            coursesPlanner,
            viewStateMapper = viewStateMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider,
            viewHistoryItemId = viewHistoryItemId,
            viewMode = viewMode
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            viewHistoryItemId: Long,
            viewMode: CardViewMode
        ): CardsViewViewModelFactory
    }
}