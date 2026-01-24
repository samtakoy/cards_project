package ru.samtakoy.domain.theme

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.theme.Theme

interface ThemesRepository {

    suspend fun getTheme(themeId: Long): Theme?
    suspend fun getThemeWithTitle(parentThemeId: Long, title: String): Theme?

    suspend fun addNewTheme(parentThemeId: Long, title: String): Theme?
    suspend fun deleteTheme(themeId: Long): Boolean
    suspend fun getChildThemes(themeId: Long): List<Theme>
    fun getChildThemesAsFlow(themeId: Long): Flow<List<Theme>>

}