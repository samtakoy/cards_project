package ru.samtakoy.core.business

import io.reactivex.Flowable
import io.reactivex.Single
import ru.samtakoy.core.database.room.entities.ThemeEntity


interface ThemesRepository {

    fun getTheme(themeId: Long): ThemeEntity?
    fun getThemeRx(themeId: Long): Single<ThemeEntity>
    fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity?

    fun addNewTheme(parentThemeId: Long, title: String): ThemeEntity?
    fun deleteTheme(themeId: Long): Boolean
    fun getChildThemes(themeId: Long): List<ThemeEntity>
    fun getChildThemesRx(themeId: Long): Flowable<List<ThemeEntity>>

}

