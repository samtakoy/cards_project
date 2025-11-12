package ru.samtakoy.presentation.utils.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module

@Composable
actual fun getKoinWithModules(vararg modules: Module): Koin {
    return remember {
        startKoin {
            modules(*modules)
        }.koin
    }
}