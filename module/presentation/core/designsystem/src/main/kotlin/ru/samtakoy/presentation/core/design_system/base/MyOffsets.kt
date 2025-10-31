package ru.samtakoy.presentation.core.design_system.base

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Временный класс, чтобы не потерять константы
 * */
object MyOffsets {
    val xxsmall: Dp = 2.dp
    val xsmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 16.dp
    val large: Dp = 24.dp
}

object UiOffsets {
    val screenContentHOffset: Dp = MyOffsets.medium
    val screenContentVOffset: Dp = MyOffsets.medium
    val listItemContentHPadding: Dp = MyOffsets.small
    val listItemContentVPadding: Dp = MyOffsets.xxsmall
    val listItemOffset: Dp = MyOffsets.small
    val dialogSurfacePaddings: Dp = MyOffsets.medium
    val dialogContentPaddings: Dp = MyOffsets.medium
    val dialogSurfaceElevation: Dp = MyOffsets.small
}