package ru.samtakoy.data.importcards.model

/**
 * @param srcPath полный путь (относительно рутовой директории с карточками) включая имя файла TODO избыточно?
 * @param parentThemeNames имена родительских тем в порядке вложености
 * @param themeId идентификатор темы, если не определен или нет, то 0L
 * @param fileName имя исходного файла
 * */
data class QPackSource(
    val content: ByteArray,
    val srcPath: String,
    val parentThemeNames: List<String>,
    val themeId: Long,
    val fileName: String,
)