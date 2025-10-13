package ru.samtakoy.data.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.samtakoy.data.theme.mapper.ThemeEntityMapper
import ru.samtakoy.domain.theme.Theme
import javax.inject.Inject

internal class ThemesRepositoryImpl @Inject constructor(
    private val themeDao: ThemeDao,
    private val themeMapper: ThemeEntityMapper
) : ThemesRepository {

    override suspend fun getTheme(themeId: Long): Theme? {
        return themeDao.getTheme(themeId)?.let(themeMapper::mapToDomain)
    }

    override fun getThemeWithTitle(parentThemeId: Long, title: String): Theme? {
        return themeDao.getThemeWithTitle(parentThemeId, title)?.let(themeMapper::mapToDomain)
    }

    override suspend fun addNewTheme(parentThemeId: Long, title: String): Theme? {
        val resultId: Long = themeDao.addTheme(ThemeEntity(0L, title, parentThemeId))
        if (resultId <= 0) {
            return null;
        }
        return themeMapper.mapToDomain(themeDao.getTheme(resultId)!!)
    }

    override fun deleteTheme(themeId: Long): Boolean {
        return themeDao.deleteThemeById(themeId) > 0
    }

    override fun getChildThemes(themeId: Long): List<Theme> {
        return themeDao.getChildThemes(themeId).map(themeMapper::mapToDomain)
    }

    override fun getChildThemesAsFlow(themeId: Long): Flow<List<Theme>> {
        return themeDao.getChildThemesAsFlow(themeId).map { it.map(themeMapper::mapToDomain) }
    }
}