package ru.samtakoy.core.presentation.base

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun getLoremIpsum(wordsCount: Int = 5): String {
    return LoremIpsum(words = wordsCount).values.toList().first().toString()
}