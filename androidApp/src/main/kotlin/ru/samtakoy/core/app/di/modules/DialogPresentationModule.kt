package ru.samtakoy.core.app.di.modules

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.samtakoy.core.presentation.progress_dialog.vm.ProgressDialogViewModelImpl

fun dialogPresentationModule() = module {
    viewModelOf(::ProgressDialogViewModelImpl)
}