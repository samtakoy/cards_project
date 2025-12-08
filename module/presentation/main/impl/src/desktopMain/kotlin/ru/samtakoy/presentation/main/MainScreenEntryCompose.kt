package ru.samtakoy.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import org.koin.core.context.GlobalContext
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.main.vm.MainScreenViewModelImpl

@Composable
fun MainScreenEntry() {
    MyTheme {
        MainScreen(
            viewModel = rememberSaveable {
                val koin = GlobalContext.get()
                MainScreenViewModelImpl(
                    contentMapper = koin.get(),
                    scopeProvider = koin.get(),
                    // TODO решение: https://github.com/JetBrains/compose-multiplatform-core/pull/2554
                    savedStateHandle = SavedStateHandle()
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}