package ru.samtakoy.features.views.presentation.history.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class OneViewHistoryItemModel(
    val id: Long,
    val qPackId: Long,
    val themeTitle: AnnotatedString,
    val packTitle: AnnotatedString,
    val viewDate: AnnotatedString,
    val viewedInfo: AnnotatedString,
    val errorCount: AnnotatedString
)