package ru.samtakoy.presentation.utils.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.module.Module

@Composable
actual fun getKoinWithModules(vararg modules: Module): Koin {
    val context = LocalContext.current
    return remember {
        val koin = KoinApplication.init()
            .androidContext(context.applicationContext)
            .modules(*modules)
        koin.koin
    }
}