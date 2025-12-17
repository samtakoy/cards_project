package ru.samtakoy.presentation.cards.screens.view.utils.codeparser

internal data class ParsedBlock(
    val blockType: Type,
    val lineStartIndex: Int,
    val lineEndIndex: Int
) {
    sealed interface Type {
        object Text : Type
        data class Code(val name: String) : Type
    }
}