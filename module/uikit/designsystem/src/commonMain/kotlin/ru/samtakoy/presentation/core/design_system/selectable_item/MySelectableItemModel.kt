package ru.samtakoy.presentation.core.design_system.selectable_item

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
data class MySelectableItemModel(
    val id: UiId,
    val text: AnnotatedString,
    val isChecked: Boolean,
    val isEnabled: Boolean,
    val contentDescription: String? = null
)