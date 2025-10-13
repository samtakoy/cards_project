package ru.samtakoy.core.presentation.themes.mv

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.themes.mapper.ThemeUiItemMapper
import ru.samtakoy.features.import_export.QPacksExporter
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.theme.ThemeInteractor

internal class ThemeListViewModelFactory @AssistedInject constructor(
    private val qPackInteractor: QPackInteractor,
    private val themeInteractor: ThemeInteractor,
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
            qPackInteractor = qPackInteractor,
            themeInteractor = themeInteractor,
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