package ru.samtakoy.oldlegacy.di.modules

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.core.presentation.progress_dialog.vm.ProgressDialogViewModelImpl

fun dialogPresentationModule() = module {
    viewModelOf(::ProgressDialogViewModelImpl)
}