package ru.samtakoy.core.data.local.reps.impl

import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.MyRoomDb
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.data.local.reps.ThemesRepository
import javax.inject.Inject

class ThemesRepositoryImpl @Inject constructor(
        private val db: MyRoomDb
) : ThemesRepository {

    override suspend fun getTheme(themeId: Long): ThemeEntity? =
            db.themeDao().getTheme(themeId)

    override fun getThemeRx(themeId: Long): Single<ThemeEntity> =
            db.themeDao().getThemeSingle(themeId)

    override fun getThemeWithTitle(parentThemeId: Long, title: String): ThemeEntity? =
            db.themeDao().getThemeWithTitle(parentThemeId, title)

    override fun addNewTheme(parentThemeId: Long, title: String): ThemeEntity? {
        val result = ThemeEntity(0L, title, parentThemeId)
        val resultId: Long = db.themeDao().addTheme(result)
        if (resultId <= 0) {
            return null;
        }
        result.id = resultId;
        return result;
    }

    override fun deleteTheme(themeId: Long): Boolean =
            db.themeDao().deleteThemeById(themeId) > 0

    override fun getChildThemes(themeId: Long): List<ThemeEntity> =
            db.themeDao().getChildThemes(themeId)

    override fun getChildThemesAsFlow(themeId: Long): Flow<List<ThemeEntity>> {
        return db.themeDao().getChildThemesAsFlow(themeId)
    }
}