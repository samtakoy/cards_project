package ru.samtakoy.core.business.impl

import io.reactivex.Flowable
import ru.samtakoy.core.business.ThemesRepository
import ru.samtakoy.core.database.room.MyRoomDb
import ru.samtakoy.core.database.room.entities.ThemeEntity

class ThemesRepositoryImpl(
        private val db: MyRoomDb
) : ThemesRepository {

    override fun getTheme(themeId: Long): ThemeEntity? =
            db.themeDao().getTheme(themeId)
    //return ContentProviderHelper.getTheme(mCtx, themeId)

    override fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity? =
            db.themeDao().getThemeWithTitle(parentThemeId, title)
    //return ContentProviderHelper.getTheme(mCtx, themeId)

    override fun addNewTheme(parentThemeId: Long, title: String): Long =
            db.themeDao().addTheme(ThemeEntity(0L, title, parentThemeId))
    //return ContentProviderHelper.addNewTheme(mCtx.getContentResolver(), parentThemeId, title)

    override fun deleteTheme(themeId: Long): Boolean =
            db.themeDao().deleteThemeById(themeId) > 0
    //return ContentProviderHelper.deleteThemeOnlyUnchecked(mCtx, themeId)

    override fun getChildThemes(themeId: Long): List<ThemeEntity> =
            db.themeDao().getChildThemes(themeId)
    //return ContentProviderHelper.getCurThemes(mCtx, themeId)

    override fun getChildThemesRx(themeId: Long): Flowable<List<ThemeEntity>> =
            db.themeDao().getChildThemesRx(themeId)
}