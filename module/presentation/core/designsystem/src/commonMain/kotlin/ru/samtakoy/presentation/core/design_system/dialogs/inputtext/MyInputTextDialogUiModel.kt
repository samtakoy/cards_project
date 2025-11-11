package ru.samtakoy.presentation.core.design_system.dialogs.inputtext

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel

@Immutable
data class MyInputTextDialogUiModel(
    val id: UiId?,
    val title: AnnotatedString,
    val description: AnnotatedString?,
    val inputHint: AnnotatedString?,
    val initialText: String,
    val okButton: MyButtonUiModel,
)