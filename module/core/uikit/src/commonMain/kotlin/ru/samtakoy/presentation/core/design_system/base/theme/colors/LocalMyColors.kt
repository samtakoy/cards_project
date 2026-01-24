package ru.samtakoy.presentation.core.design_system.base.theme.colors

import androidx.compose.runtime.staticCompositionLocalOf

val LocalMyColors = staticCompositionLocalOf<MyColors> {
    error("No colors provided")
}