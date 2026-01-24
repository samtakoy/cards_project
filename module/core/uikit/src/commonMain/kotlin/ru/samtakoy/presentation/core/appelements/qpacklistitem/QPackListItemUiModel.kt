package ru.samtakoy.presentation.core.appelements.qpacklistitem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.model.LongUiId

@Immutable
data class QPackListItemUiModel(
    val id: LongUiId,
    val title: AnnotatedString,
    val creationDate: AnnotatedString,
    val viewCount: AnnotatedString? = null
)