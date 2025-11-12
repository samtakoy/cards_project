package ru.samtakoy.presentation.utils.preview

import androidx.compose.runtime.Composable
import org.koin.core.Koin
import org.koin.core.module.Module

@Composable
expect fun getKoinWithModules(vararg modules: Module): Koin