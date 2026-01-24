package ru.samtakoy.presentation.qpacks.screens.fastlist.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.base.model.LongUiId

@Immutable
data class FastCardUiModel(
    val id: LongUiId,
    val question: AnnotatedString,
    val answer: AnnotatedString
)