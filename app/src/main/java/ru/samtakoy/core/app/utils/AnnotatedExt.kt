package ru.samtakoy.core.app.utils

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