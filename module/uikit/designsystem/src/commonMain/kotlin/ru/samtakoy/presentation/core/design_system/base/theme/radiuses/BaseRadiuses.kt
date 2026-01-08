package ru.samtakoy.presentation.core.design_system.base.theme.radiuses

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class BaseRadiuses(
    val r8: Dp = 8.dp,
    val r16: Dp = 16.dp
)

val DefaultBaseRadiuses = BaseRadiuses()