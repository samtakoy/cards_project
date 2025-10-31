package ru.samtakoy.presentation.core.design_system.dialogs

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel

@Immutable
data class MyAlertDialogUiModel(
    val id: UiId?,
    val title: AnnotatedString,
    val description: AnnotatedString?,
    val buttons: ImmutableList<MyButtonUiModel>
)