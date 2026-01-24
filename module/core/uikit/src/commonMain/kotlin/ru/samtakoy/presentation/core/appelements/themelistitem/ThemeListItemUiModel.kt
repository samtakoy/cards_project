package ru.samtakoy.presentation.core.appelements.themelistitem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.model.LongUiId

@Immutable
data class ThemeListItemUiModel(
    val id: LongUiId,
    val title: AnnotatedString
)