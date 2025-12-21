package ru.samtakoy.presentation.cards.screens.view.vm.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.presentation.cards.screens.view.model.BackupInfo
import ru.samtakoy.presentation.cards.screens.view.model.CodeType
import ru.samtakoy.presentation.cards.screens.view.model.ContentPart
import ru.samtakoy.presentation.cards.screens.view.model.hasBackup
import ru.samtakoy.presentation.cards.screens.view.utils.codeparser.InBraceBlockParser
import ru.samtakoy.presentation.cards.screens.view.utils.codeparser.MarkedBlockParser
import ru.samtakoy.presentation.cards.screens.view.utils.codeparser.ParsedBlock
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
                parts = parseContentParts(card.question).toImmutableList(),
                hasRevertButton = backup.hasBackup(isQuestion)
            )
        } else {
            CardsViewViewModel.CardState.Content(
                isFavorite = card.favorite > 0,
                parts = parseContentParts(card.answer).toImmutableList(),
                hasRevertButton = backup.hasBackup(isQuestion)
            )
        }
    }

    /** Ленивая реализация поиска участков кода внутри скобок {}.
     * Разметка в тексте карточек не была предусмотрена с самого начала и поэтому для старых карточек
     * адгоритм просто ище строки текста в которых есть открывающая и закрывающая фигурные скобки
     * */
    private fun parseContentParts(rawText: String): List<ContentPart> {
        val lines = rawText.split("\n")
        val blocks = markedBlockParser.parse(lines)

        if (blocks.isEmpty()) {
            return emptyList()
        }

        if (blocks.size > 1 || blocks[0].blockType is ParsedBlock.Type.Code) {
            // распознана markdown разметка
            return mapToContentParts(blocks, lines)
        }

        // Пробуем упрощенный парсинг кода без разметки.
        return mapToContentParts(
            blocks = inBraceBlockParser.parse(lines, OLD_PARSER_CODE_NAME),
            lines = lines
        )
    }

    private fun mapToContentParts(blocks: List<ParsedBlock>, lines: List<String>): List<ContentPart> {
        return blocks.map { block ->
            when (val blockType = block.blockType) {
                is ParsedBlock.Type.Code -> {
                    ContentPart.Code(
                        value = extractText(block, lines),
                        type = resoleType(blockType.name),
                        typeLabel = blockType.name.takeIf { it != OLD_PARSER_CODE_NAME }.orEmpty()
                    )
                }
                ParsedBlock.Type.Text -> {
                    ContentPart.Text(extractText(block, lines).asA())
                }
            }
        }
    }

    private fun resoleType(name: String): CodeType {
        return when (name.lowercase()) {
            OLD_PARSER_CODE_NAME -> CodeType.AutoParsedKotlin
            LANG_KOTLIN -> CodeType.Kotlin
            LANG_SWIFT -> CodeType.Swift
            LANG_OBJC, LANG_OBJECTIVEC -> CodeType.ObjectiveC
            LANG_C -> CodeType.C
            LANG_CPP -> CodeType.CPP
            LANG_XML, LANG_TEXT -> CodeType.Text
            LANG_JAVA -> CodeType.Java
            LANG_JAVASCRIPT -> CodeType.JavaScript
            else -> CodeType.AllKeywords
        }
    }

    private fun extractText(
        block: ParsedBlock,
        lines: List<String>
    ): String {
        return lines.subList(block.lineStartIndex, block.lineEndIndex + 1).joinToString(LINE_BREAK)
    }

    companion object {
        private val markedBlockParser = MarkedBlockParser()
        private val inBraceBlockParser = InBraceBlockParser()
        private const val OLD_PARSER_CODE_NAME = "_auto_kotlin_"
        private const val LANG_KOTLIN = "kotlin"
        private const val LANG_SWIFT = "swift"
        private const val LANG_TEXT = "text"
        private const val LANG_JAVA = "java"
        private const val LANG_JAVASCRIPT = "javascript"
        private const val LANG_XML = "xml"
        private const val LANG_OBJC = "objc"
        private const val LANG_C = "c"
        private const val LANG_CPP = "cpp"
        private const val LANG_OBJECTIVEC = "objectivec"
        private const val LINE_BREAK = "\n"
    }
}