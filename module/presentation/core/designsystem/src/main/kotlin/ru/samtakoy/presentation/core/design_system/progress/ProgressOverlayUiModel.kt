package ru.samtakoy.presentation.core.design_system.progress

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class ProgressOverlayUiModel(
    val title: AnnotatedString,
    val subtitle: AnnotatedString?
)