package ru.samtakoy.presentation.core.design_system.base.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun getRoundedShape(radius: Dp): Shape {
    return RoundedCornerShape(radius)
}

@Stable
fun getLeftRoundedShape(radius: Dp): Shape {
    return RoundedCornerShape(
        topStart = radius,
        bottomStart = radius,
        topEnd = 0.dp,
        bottomEnd = 0.dp
    )
}