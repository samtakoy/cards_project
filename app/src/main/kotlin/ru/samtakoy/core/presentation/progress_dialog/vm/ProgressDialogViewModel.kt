package ru.samtakoy.core.presentation.progress_dialog.vm

import androidx.compose.runtime.Immutable
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModel.Action
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModel.State

@Immutable
internal interface ProgressDialogViewModel : BaseViewModel<State, Action, Unit> {
    @Immutable
    data class State(
        val title: String
    )
    sealed interface Action {
        class ShowErrorMessage(val message: String) : Action
        object ExitCanceled : Action
        object ExitOk : Action
    }
}