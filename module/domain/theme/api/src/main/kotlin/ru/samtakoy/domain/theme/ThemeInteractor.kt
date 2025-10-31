package ru.samtakoy.domain.theme

import kotlinx.coroutines.flow.Flow

interface ThemeInteractor {
    suspend fun addNewTheme(parentThemeId: Long, title: String): Theme?

    suspend fun deleteTheme(themeId: Long): Boolean

    suspend fun getTheme(themeId: Long): Theme?

    fun getChildThemesAsFlow(themeId: Long): Flow<List<Theme>>

    /** Проверяет путь из вложенных тем.
     * Если какой-то темы ен существует - создает ее.
     * Возвращает идентификатор темы в конце пути, либо 0L для рутовой темы
     * */
    suspend fun actualizeThemePath(themePath: List<String>): Long
}