package ru.samtakoy.presentation.cards.screens.view.utils.codeparser

internal class InBraceBlockParser {

    fun parse(lines: List<String>, codeName: String): List<ParsedBlock> {
        val allClosedBlocks = mutableListOf<BraceBlock>()
        // Храним индексы строк открывающих скобок
        val stack = mutableListOf<Int>()

        // 1. Находим все пары закрытых скобок {}
        for ((lineIndex, line) in lines.withIndex()) {
            // Пропустим потенциальный однострочный комментарий.
            // Многострочный обработать мы не хотим, т.к. это заранее ленивая реализация
            // по принципу "лишь бы что-то похожее на правду увидеть".
            // (мы не ожидаем в старых карточках таких сложных конструкций в примерах кода,
            //  а в новых будет новая разметка кода через markdown)

            if (COMMENT_REGEX_PATTERN.matchEntire(line) != null) {
                // вся - строка комментарий
                // интерпретируем как блок скобок
                allClosedBlocks.add(BraceBlock(lineIndex, lineIndex))
                continue
            }

            val startCommentIndex = line.indexOf(COMMENT_PATTERN)
            for ((charIndex, char) in line.withIndex()) {
                if (startCommentIndex >= 0 && charIndex >= startCommentIndex) {
                    break
                } else if (char == '{') {
                    stack.add(lineIndex)
                } else if (char == '}') {
                    if (stack.isNotEmpty()) {
                        val startLine = stack.removeAt(stack.lastIndex)
                        allClosedBlocks.add(BraceBlock(startLine, lineIndex))
                    }
                }
            }
        }

        // 2. Оставляем только те, которые являются "верхними" среди закрытых
        // Т.к. вложенные блоки закрываются раньше чем внешние,
        // то результат формируем с конца (с гарантированно внешних блоков).

        // Т.к. мы собираем индексы с блоками кода, то допустимо, что два блока на одной строке,
        // вроде { ... } .. { ... } попадут в результат только один раз.

        val topLevelOpenCloseMap = mutableMapOf<Int, Int>()
        var lastOpenIndex = Int.MAX_VALUE
        for (idx in allClosedBlocks.lastIndex downTo 0) {
            val block = allClosedBlocks[idx]
            if (block.startLineIndex < lastOpenIndex) {
                // Это закрытый блок который начался на строке выше, чем любой последний добавленнный
                lastOpenIndex = block.startLineIndex
                topLevelOpenCloseMap[block.startLineIndex] = block.endLineIndex
            }
        }

        // 3. Формируем результат.
        return formatResult(
            lines = lines,
            openCloseMap = topLevelOpenCloseMap,
            codeName = codeName
        )
    }

    private fun formatResult(
        lines: List<String>,
        openCloseMap: MutableMap<Int, Int>,
        codeName: String
    ): List<ParsedBlock> {
        val result = mutableListOf<ParsedBlock>()
        var currentBlock: ParsedBlock? = null

        var lineIndex = 0
        while(lineIndex <= lines.lastIndex) {
            var closeIndex = openCloseMap[lineIndex]
            if (closeIndex == null) {
                // текст
                if (currentBlock == null) {
                    currentBlock = ParsedBlock(
                        blockType = ParsedBlock.Type.Text,
                        lineStartIndex = lineIndex,
                        lineEndIndex = lineIndex
                    )
                }
                // Дальше
                lineIndex++
            } else {
                // Блок кода
                if (currentBlock != null) {
                    // Текстовый блок закроем на предыдущей строке
                    result.add(currentBlock.copy(lineEndIndex = lineIndex-1))
                }
                currentBlock = ParsedBlock(
                    blockType = ParsedBlock.Type.Code(codeName),
                    lineStartIndex = lineIndex,
                    lineEndIndex = closeIndex
                )
                lineIndex = closeIndex
                // Все соседние блоки кода объединим в один
                do {
                        // Т.е. те, которые начинаются, где заканчивается текущий блок, либо со следующей строки.
                    closeIndex = openCloseMap[lineIndex].takeIf { it != lineIndex } ?: openCloseMap[lineIndex + 1]
                    if (closeIndex != null) {
                        currentBlock = currentBlock?.copy(lineEndIndex = closeIndex)
                        lineIndex = closeIndex
                    }
                } while (closeIndex != null)

                result.add(currentBlock!!)
                // Дальше
                currentBlock = null
                lineIndex++
            }
        }
        if (currentBlock != null) {
            // Текстовый блок закроем
            result.add(currentBlock.copy(lineEndIndex = lines.lastIndex))
        }
        return result
    }

    private data class BraceBlock(
        val startLineIndex: Int,
        val endLineIndex: Int
    )

    companion object {
        private val COMMENT_PATTERN = "//"
        private val COMMENT_REGEX_PATTERN = "^\\s*//.*$".toRegex()
    }
}