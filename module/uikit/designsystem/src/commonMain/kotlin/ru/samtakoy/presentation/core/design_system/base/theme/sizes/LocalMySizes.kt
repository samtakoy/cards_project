package ru.samtakoy.presentation.core.design_system.base.theme.sizes

import androidx.compose.runtime.staticCompositionLocalOf

val LocalMySizes = staticCompositionLocalOf<MySizes> {
    error("No sizes provided")
}