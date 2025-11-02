package ru.samtakoy.presentation.core.design_system.radio

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
data class MyRadioItemUiModel(
    val id: UiId,
    val text: AnnotatedString,
    val isSelected: Boolean,
    val isEnabled: Boolean = true,
    val contentDescription: String? = null
)