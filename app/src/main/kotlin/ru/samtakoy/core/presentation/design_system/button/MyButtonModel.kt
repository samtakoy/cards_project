package ru.samtakoy.core.presentation.design_system.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.core.presentation.design_system.base.model.UiId

@Immutable
data class MyButtonModel(
    val id: UiId,
    val text: AnnotatedString,
    val isEnabled: Boolean = true
)