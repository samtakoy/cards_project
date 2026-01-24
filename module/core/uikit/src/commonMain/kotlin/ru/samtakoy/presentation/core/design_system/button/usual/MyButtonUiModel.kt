package ru.samtakoy.presentation.core.design_system.button.usual

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.model.UiId

@Immutable
data class MyButtonUiModel(
    val id: UiId,
    val text: AnnotatedString,
    val isEnabled: Boolean = true,
    val type: Type = Type.Regular
) {
    enum class Type {
        SmallSwitcher,
        Regular
    }
}