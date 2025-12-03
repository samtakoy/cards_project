package ru.samtakoy.oldlegacy.core.presentation.export_cards.mv

import ru.samtakoy.presentation.base.viewmodel.BaseViewModel
import ru.samtakoy.oldlegacy.core.presentation.export_cards.mv.BatchExportViewModel.Action

interface BatchExportViewModel : BaseViewModel<Unit, Action, Unit> {
    sealed interface Action {
        object ExitOk : Action
        class ExitWithError(val errorText: String) : Action
    }
}