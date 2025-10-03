package ru.samtakoy.core.presentation.themes.mv

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapper
import ru.samtakoy.features.import_export.QPacksExporter

internal class ThemeListViewModelFactory @AssistedInject constructor(
    private val cardsInteractor: CardsInteractor,
    private val qPacksExporter: QPacksExporter,
    private val uiItemsMapper: ThemeUiItemMapper,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val themeId: Long,
    @Assisted
    private val themeTitle: String
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        @Suppress("UNCHECKED_CAST")
        return ThemeListViewModelImpl(
            cardsInteractor = cardsInteractor,
            qPacksExporter = qPacksExporter,
            uiItemsMapper = uiItemsMapper,
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider,
            themeId = themeId,
            themeTitle = themeTitle
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            themeId: Long,
            themeTitle: String
        ): ThemeListViewModelFactory
    }
}