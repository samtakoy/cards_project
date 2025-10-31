package ru.samtakoy.presentation.core.design_system.base.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Stable
fun getRoundedShape(radius: Dp): Shape {
    return RoundedCornerShape(radius)
}