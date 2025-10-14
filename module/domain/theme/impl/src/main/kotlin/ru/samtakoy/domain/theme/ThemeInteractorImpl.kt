package ru.samtakoy.domain.theme

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.theme.ThemesRepository
import ru.samtakoy.domain.qpack.QPackInteractor
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