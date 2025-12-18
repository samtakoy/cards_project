package ru.samtakoy.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import org.koin.compose.koinInject
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.main.vm.MainScreenViewModelImpl
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper

@Composable
fun MainScreenEntry() {
    MyTheme {
        val contentMapper = koinInject<MainScreenContentMapper>()
        val scopeProvider = koinInject<ScopeProvider>()
        MainScreen(
            viewModel = rememberSaveable {
                MainScreenViewModelImpl(
                    contentMapper = contentMapper,
                    scopeProvider = scopeProvider,
                    // TODO решение: https://github.com/JetBrains/compose-multiplatform-core/pull/2554
                    savedStateHandle = SavedStateHandle()
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}