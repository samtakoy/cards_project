package ru.samtakoy.core.business

import io.reactivex.Flowable
import ru.samtakoy.core.database.room.entities.ThemeEntity


interface ThemesRepository {

    fun getTheme(themeId: Long): ThemeEntity?
    fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity?

    fun addNewTheme(parentThemeId: Long, title: String): Long
    fun deleteTheme(themeId: Long): Boolean
    fun getChildThemes(themeId: Long): List<ThemeEntity>
    fun getChildThemesRx(themeId: Long): Flowable<List<ThemeEntity>>
    //Theme getParentTheme(Long themeId);
}

