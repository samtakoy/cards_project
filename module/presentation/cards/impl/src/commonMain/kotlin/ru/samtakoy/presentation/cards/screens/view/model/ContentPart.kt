package ru.samtakoy.presentation.cards.screens.view.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
internal sealed interface ContentPart {
    data class Text(val value: AnnotatedString) : ContentPart
    data class Code(val value: String) : ContentPart
}