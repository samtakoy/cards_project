package ru.samtakoy.presentation.core.design_system.dialogs.choice

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemUiModel

@Immutable
data class MyChoiceDialogUiModel(
    val id: UiId?,
    val title: AnnotatedString,
    val description: AnnotatedString?,
    val items: ImmutableList<MyRadioItemUiModel>,
    val firstButton: MyButtonUiModel,
    val secondButton: MyButtonUiModel? = null,
)