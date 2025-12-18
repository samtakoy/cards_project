package ru.samtakoy.presentation.cards.screens.view.utils.codeparser

/**
 * Ищет блоки разметки кода через markdown вида:
 * ```kotlin
 *  // code
 * ```
 * @param lines строки исходного текста
 * @return результирующий список блоков
 * */
internal class MarkedBlockParser {

    fun parse(lines: List<String>): List<ParsedBlock> {

        val result = mutableListOf<ParsedBlock>()
        var currentBlock: ParsedBlock? = null

        lines.forEachIndexed { index, line ->
            when (currentBlock?.blockType) {
                is ParsedBlock.Type.Code -> {
                    if(CODE_END_PATTERN.matchEntire(line) != null) {
                        // закрыть блок кода
                        currentBlock = currentBlock.copy(lineEndIndex = index - 1)
                        result.addIfValid(currentBlock)
                        currentBlock = null
                    }
                }
                ParsedBlock.Type.Text -> {
                    val matchResult = CODE_START_PATTERN.matchEntire(line)
                    if (matchResult != null && index < lines.lastIndex) {
                        // закрыть текстовый блок на предыдущей строке
                        currentBlock = currentBlock.copy(lineEndIndex = index - 1)
                        result.addIfValid(currentBlock)

                        // начать блок кода
                        currentBlock = getNewCodeBlock(matchResult, index)
                    }
                }
                null -> {
                    // Начало нового блока
                    val matchResult = CODE_START_PATTERN.matchEntire(line)
                    currentBlock = if (matchResult == null || index == lines.lastIndex) {
                        // нет открытия блока кода, либо уже последняя строка - обычный текст
                        getNewTextBlock(index)
                    } else {
                        getNewCodeBlock(matchResult, index)
                    }
                }
            }
        }
        // Пусть пока незакрытые блоки кода автозакрываются...
        if (currentBlock != null) {
            currentBlock = currentBlock.copy(lineEndIndex = lines.lastIndex)
            result.addIfValid(currentBlock)
        }
        return result
    }

    /**
     * Создать блок текста, который был обнаружен на currentLineIndex
     * */
    private fun getNewTextBlock(currentLineIndex: Int): ParsedBlock {
        return ParsedBlock(
            blockType = ParsedBlock.Type.Text,
            lineStartIndex = currentLineIndex,
            lineEndIndex = currentLineIndex
        )
    }

    /**
     * Создать блок кода, который был обнаружен на currentLineIndex
     * */
    private fun getNewCodeBlock(
        codeStartMatchResult: MatchResult,
        currentLineIndex: Int
    ): ParsedBlock = ParsedBlock(
        blockType = ParsedBlock.Type.Code(codeStartMatchResult.groups.get(1)?.value.orEmpty()),
        // старт контента со следующей строки
        lineStartIndex = currentLineIndex + 1,
        lineEndIndex = currentLineIndex + 1
    )

    private fun MutableList<ParsedBlock>.addIfValid(block: ParsedBlock) {
        // выбросим блоки кода, которые оказались пустыми
        if (block.lineEndIndex - block.lineStartIndex >= 0) {
            add(block)
        }
    }

    companion object {
        /**
         * 2-3 апострофа, за которым следует слово, затем возможные пробельные символы и конец строки.
         * Пробелы в начале допускаются.
         * */
        private val CODE_START_PATTERN = "^\\s*`{2,3}(\\w*)\\s*$".toRegex()
        /**
         * Два апострофа и более, за которыми следуют только пробельные символы и конец строки.
         * Пробелы в начале допускаются.
         * */
        private val CODE_END_PATTERN = "^\\s*`{2,}\\s*$".toRegex()
    }
}