package ru.samtakoy.core.presentation.progress_dialog.vm

import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter.IProgressWorker
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModel.Action
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModel.State

internal class ProgressDialogViewModelImpl(
    scopeProvider: ScopeProvider,
    private val worker: IProgressWorker
) : BaseViewModelImpl<State, Action, Unit>(
    scopeProvider = scopeProvider,
    initialState = State(
        title = worker.getTitle()
    )
), ProgressDialogViewModel {

    init {
        launchCatching(
            onError = {
                sendAction(Action.ShowErrorMessage(worker.getErrorText()))
                sendAction(Action.ExitOk)
            }
        ) {
            worker.doWork()
            worker.onComplete()
            sendAction(Action.ExitCanceled)
        }

    }

    override fun onEvent(event: Unit) = Unit
}