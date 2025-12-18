package ru.samtakoy.presentation.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString

@Stable
fun CharSequence.asAnnotated(): AnnotatedString {
    return if (this is AnnotatedString) {
        this
    } else {
        AnnotatedString(this.toString())
    }
}

@Stable
fun CharSequence.asA(): AnnotatedString {
    return if (this is AnnotatedString) {
        this
    } else {
        AnnotatedString(this.toString())
    }
}