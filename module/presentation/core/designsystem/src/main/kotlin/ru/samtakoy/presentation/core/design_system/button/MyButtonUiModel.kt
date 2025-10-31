package ru.samtakoy.presentation.core.design_system.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
data class MyButtonUiModel(
    val id: UiId,
    val text: AnnotatedString,
    val isEnabled: Boolean = true
)