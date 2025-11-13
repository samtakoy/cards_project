package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.presentation.cards.screens.view.model.BackupInfo
import ru.samtakoy.presentation.cards.screens.view.model.ContentPart
import ru.samtakoy.presentation.cards.screens.view.model.hasBackup
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel
import ru.samtakoy.presentation.utils.asA

internal interface CardContentMapper {
    fun map(card: Card, isQuestion: Boolean, backup: BackupInfo): CardsViewViewModel.CardState.Content
}

internal class CardContentMapperImpl : CardContentMapper {
    override fun map(
        card: Card,
        isQuestion: Boolean,
        backup: BackupInfo
    ): CardsViewViewModel.CardState.Content {
        return if (isQuestion) {
            CardsViewViewModel.CardState.Content(
                isFavorite = card.favorite > 0,
                parts = mapContentParts(card.question),
                hasRevertButton = backup.hasBackup(isQuestion)
            )
        } else {
            CardsViewViewModel.CardState.Content(
                isFavorite = card.favorite > 0,
                parts = mapContentParts(card.answer),
                hasRevertButton = backup.hasBackup(isQuestion)
            )
        }
    }

    private fun mapContentParts(text: String): ImmutableList<ContentPart> {
        return parseKotlinCode(rawText = text).toImmutableList()
    }

    /** Ленивая реализация поиска участков кода внутри скобок {}.
     * Разметка в тексте карточек не была предусмотрена...
     * */
    private fun parseKotlinCode(rawText: String): List<ContentPart> {
        val parts = mutableListOf<ContentPart>()
        val lines = rawText.split("\n")

        // Найдем все блоки кода (строки со скобками)
        val codeBlockRanges = findTopLevelBraces(lines)

        var currentPos = 0
        for ((startLine, endLine) in codeBlockRanges) {
            // Добавляем текст до начала кода
            if (currentPos < startLine) {
                val textLines = lines.subList(currentPos, startLine)
                val textContent = textLines.joinToString("\n").trim()
                if (textContent.isNotEmpty()) {
                    parts.add(ContentPart.Text(textContent.asA()))
                }
            }

            // Добавляем блок кода
            val codeLines = lines.subList(startLine, endLine + 1)
            val codeContent = codeLines.joinToString("\n").trim()
            if (codeContent.isNotEmpty()) {
                parts.add(ContentPart.Code(codeContent))
            }

            currentPos = endLine + 1
        }

        // Добавляем оставшийся текст
        if (currentPos < lines.size) {
            val textLines = lines.subList(currentPos, lines.size)
            val textContent = textLines.joinToString("\n").trim()
            if (textContent.isNotEmpty()) {
                parts.add(ContentPart.Text(textContent.asA()))
            }
        }

        return parts
    }

    /**
     * Находит все top-level блоки кода (парные скобки, не вложенные в другие)
     * Возвращает список пар (startLineIndex, endLineIndex)
     */
    private fun findTopLevelBraces(lines: List<String>): List<Pair<Int, Int>> {
        val codeBlockRanges = mutableListOf<Pair<Int, Int>>()
        var braceCount = 0
        var blockStartLine = -1

        for ((lineIndex, line) in lines.withIndex()) {
            val trimmed = line.trim()

            // Пропускаем пустые строки и комментарии
            if (trimmed.isEmpty() || trimmed.startsWith("//")) {
                continue
            }

            // Ищем открывающие и закрывающие скобки на этой строке
            for (char in line) {
                when (char) {
                    '{' -> {
                        if (braceCount == 0) {
                            // Начало нового top-level блока
                            blockStartLine = lineIndex
                        }
                        braceCount++
                    }
                    '}' -> {
                        braceCount--
                        if (braceCount == 0 && blockStartLine != -1) {
                            // Конец top-level блока
                            codeBlockRanges.add(blockStartLine to lineIndex)
                            blockStartLine = -1
                        }
                    }
                }
            }
        }

        return codeBlockRanges
    }
}