package ru.samtakoy.features.theme.domain

import kotlinx.coroutines.flow.Flow

interface ThemeInteractor {
    suspend fun addNewTheme(parentThemeId: Long, title: String): Theme?

    suspend fun deleteTheme(themeId: Long): Boolean

    suspend fun getTheme(themeId: Long): Theme?

    fun getChildThemesAsFlow(themeId: Long): Flow<List<Theme>>
}