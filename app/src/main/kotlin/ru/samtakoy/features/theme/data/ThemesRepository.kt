package ru.samtakoy.features.theme.data

import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.theme.domain.Theme

interface ThemesRepository {

    suspend fun getTheme(themeId: Long): Theme?
    fun getThemeRx(themeId: Long): Single<Theme>
    fun getThemeWithTitle(parentThemeId: Long, title: String): Theme?

    suspend fun addNewTheme(parentThemeId: Long, title: String): Theme?
    fun deleteTheme(themeId: Long): Boolean
    fun getChildThemes(themeId: Long): List<Theme>
    fun getChildThemesAsFlow(themeId: Long): Flow<List<Theme>>

}

