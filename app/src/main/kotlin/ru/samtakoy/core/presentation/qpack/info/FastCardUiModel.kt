package ru.samtakoy.core.presentation.qpack.info

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.core.presentation.design_system.base.model.LongUiId

@Immutable
data class FastCardUiModel(
    val id: LongUiId,
    val question: AnnotatedString,
    val answer: AnnotatedString
)