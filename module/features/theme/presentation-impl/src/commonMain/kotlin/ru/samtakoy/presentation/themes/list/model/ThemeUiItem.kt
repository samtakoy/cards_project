package ru.samtakoy.presentation.themes.list.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.model.LongUiId

@Immutable
internal sealed interface ThemeUiItem {
    val composeKey: String

    @Immutable
    data class Theme(
        override val composeKey: String,
        val id: LongUiId,
        val title: AnnotatedString
    ) : ThemeUiItem

    @Immutable
    data class QPack(
        override val composeKey: String,
        val id: LongUiId,
        val title: AnnotatedString,
        val creationDate: AnnotatedString,
        val viewCount: AnnotatedString?
    ) : ThemeUiItem
}