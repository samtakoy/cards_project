package ru.samtakoy.core.presentation.export_cards.mv

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.core.presentation.export_cards.BatchExportType
import ru.samtakoy.features.import_export.CoursesExporter
import ru.samtakoy.features.import_export.QPacksExporter

internal class BatchExportViewModelFactory @AssistedInject constructor(
    private val coursesExporter: CoursesExporter,
    private val qPacksExporter: QPacksExporter,
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val type: BatchExportType
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == BatchExportViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return BatchExportViewModelImpl(
            coursesExporter = coursesExporter,
            qPacksExporter = qPacksExporter,
            resources = resources,
            scopeProvider = scopeProvider,
            type = type
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun factory(
            type: BatchExportType
        ): BatchExportViewModelFactory
    }
}