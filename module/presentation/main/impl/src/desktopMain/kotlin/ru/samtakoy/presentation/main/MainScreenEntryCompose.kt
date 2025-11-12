package ru.samtakoy.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.main.vm.MainScreenViewModelImpl

@Composable
fun MainScreenEntry() {
    MyTheme {
        MainScreen(
            viewModel = koinViewModel<MainScreenViewModelImpl>(),
            modifier = Modifier.Companion.fillMaxSize()
        )
    }
}