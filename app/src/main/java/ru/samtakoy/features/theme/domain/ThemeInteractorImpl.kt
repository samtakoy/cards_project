package ru.samtakoy.features.theme.domain

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.qpack.domain.QPackInteractor
import ru.samtakoy.features.theme.data.ThemesRepository
import javax.inject.Inject

class ThemeInteractorImpl @Inject constructor(
    private val themeRepository: ThemesRepository,
    private val qPackInteractor: QPackInteractor
) : ThemeInteractor {

    override suspend fun addNewTheme(parentThemeId: Long, title: String): Theme {
        return themeRepository.addNewTheme(parentThemeId, title)!!
    }

    // TODO пока удаление, только если тема пустая, работает молча
    override suspend fun deleteTheme(themeId: Long): Boolean {
        if (
            qPackInteractor.getQPacksFromThemeCount(themeId) > 0 ||
            themeRepository.getChildThemes(themeId).size > 0
        ) {
            return false
        }
        return themeRepository.deleteTheme(themeId)
    }

    override suspend fun getTheme(themeId: Long): Theme? {
        return themeRepository.getTheme(themeId)
    }

    override fun getChildThemesAsFlow(themeId: Long): Flow<List<Theme>> {
        return themeRepository.getChildThemesAsFlow(themeId)
    }
}