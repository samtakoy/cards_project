package ru.samtakoy.core.presentation.themes

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId

@Immutable
internal sealed interface ThemeUiItem {
    @Immutable
    data class Theme(
        val id: LongUiId,
        val title: AnnotatedString
    ) : ThemeUiItem
    @Immutable
    data class QPack(
        val id: LongUiId,
        val title: AnnotatedString,
        val creationDate: AnnotatedString
    ) : ThemeUiItem
}