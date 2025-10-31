package ru.samtakoy.presentation.core.design_system.base.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(content = content)
}