package ru.samtakoy.presentation.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

@Stable
fun getLoremIpsum(wordsCount: Int = 5): String {
    return LoremIpsum(words = wordsCount).values.toList().first().toString()
}

@Stable
fun getALoremIpsum(wordsCount: Int = 5): AnnotatedString {
    return getLoremIpsum(wordsCount = wordsCount).asAnnotated()
}