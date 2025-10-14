package ru.samtakoy.core.presentation.progress_dialog.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker

internal class ProgressDialogViewModelFactory @AssistedInject constructor(
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val worker: IProgressWorker
): AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == ProgressDialogViewModelImpl::class.java)
        @Suppress("UNCHECKED_CAST")
        return ProgressDialogViewModelImpl(
            scopeProvider = scopeProvider,
            worker = worker
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(worker: IProgressWorker): ProgressDialogViewModelFactory
    }
}