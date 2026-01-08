package ru.samtakoy.presentation.core.design_system.base.theme.offsets

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class BaseOffsets(
    val xxsmall: Dp = 2.dp,
    val xsmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp
)

val DefaultBaseOffsets = BaseOffsets()