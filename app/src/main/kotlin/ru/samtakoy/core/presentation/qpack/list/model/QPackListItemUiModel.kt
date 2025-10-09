package ru.samtakoy.core.presentation.qpack.list.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId

@Immutable
data class QPackListItemUiModel(
    val id: LongUiId,
    val title: AnnotatedString,
    val dateText: AnnotatedString,
    val viewCountText: AnnotatedString
)