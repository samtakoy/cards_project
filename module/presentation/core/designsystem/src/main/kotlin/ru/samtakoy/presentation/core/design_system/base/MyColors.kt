package ru.samtakoy.presentation.core.design_system.base

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
    fun getListItemBgColor(): Color {
        return MaterialTheme.colorScheme.surfaceVariant
    }

    @Composable
    @ReadOnlyComposable
    fun getListItemBgLightColor(): Color {
        return MaterialTheme.colorScheme.surface
    }

    @Composable
    @ReadOnlyComposable
    fun getListItemOddBgColor(): Color {
        return getListItemBgLightColor()
    }

    @Composable
    @ReadOnlyComposable
    fun getListItemEvenBgColor(): Color {
        return getListItemBgColor()
    }

    @Composable
    @ReadOnlyComposable
    fun getErrorTextColor(): Color {
        return Color(0xffB34455)
    }

    @Composable
    @ReadOnlyComposable
    fun getOverlayColor(): Color {
        return MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f)
    }
}