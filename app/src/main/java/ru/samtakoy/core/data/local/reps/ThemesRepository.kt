package ru.samtakoy.core.data.local.reps

import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity


interface ThemesRepository {

    suspend fun getTheme(themeId: Long): ThemeEntity?
    fun getThemeRx(themeId: Long): Single<ThemeEntity>
    fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity?

    fun addNewTheme(parentThemeId: Long, title: String): ThemeEntity?
    fun deleteTheme(themeId: Long): Boolean
    fun getChildThemes(themeId: Long): List<ThemeEntity>
    fun getChildThemesAsFlow(themeId: Long): Flow<List<ThemeEntity>>

}

