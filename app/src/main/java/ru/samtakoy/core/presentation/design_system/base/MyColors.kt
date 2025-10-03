package ru.samtakoy.core.presentation.design_system.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/**
 * Временный класс, чтобы не потерять константы
 * */
object MyColors {
    val backgroundLoaderBgColor: Color = Color(0x22B3B3FF)

    @Composable
    @ReadOnlyComposable
    fun getScreenBackground(): Color {
        return MaterialTheme.colorScheme.background
    }

    @Composable
    @ReadOnlyComposable
    fun getListItemOddBgColor(): Color {
        return MaterialTheme.colorScheme.surface
    }

    @Composable
    @ReadOnlyComposable
    fun getListItemEvenBgColor(): Color {
        return MaterialTheme.colorScheme.surfaceVariant
    }

    @Composable
    @ReadOnlyComposable
    fun getErrorTextColor(): Color {
        return Color(0xffB34455)
    }
}