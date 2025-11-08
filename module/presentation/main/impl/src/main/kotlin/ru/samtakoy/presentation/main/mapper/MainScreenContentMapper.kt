package ru.samtakoy.presentation.main.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdCourseListRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdFavoritesRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdQPackListRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdQPackSelectionRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdSettingsRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdThemeListRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdViewsHistoryRoute
import ru.samtakoy.presentation.main.vm.MainScreenViewModel
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.main_courses_title
import ru.samtakoy.resources.main_favorites_title
import ru.samtakoy.resources.main_qpack_selection_title
import ru.samtakoy.resources.main_qpack_title
import ru.samtakoy.resources.main_settings_title
import ru.samtakoy.resources.main_themes_list_title
import ru.samtakoy.resources.main_views_history_title

internal interface MainScreenContentMapper {
    suspend fun mapMenuItems(): List<MainScreenViewModel.MenuItem>

    companion object {
        val IdThemeListRoute = AnyUiId()
        val IdQPackListRoute = AnyUiId()
        val IdFavoritesRoute = AnyUiId()
        val IdViewsHistoryRoute = AnyUiId()
        val IdQPackSelectionRoute = AnyUiId()
        val IdCourseListRoute = AnyUiId()
        val IdSettingsRoute = AnyUiId()
    }
}

internal class MainScreenContentMapperImpl: MainScreenContentMapper {
    override suspend fun mapMenuItems(): List<MainScreenViewModel.MenuItem> {
        return listOf(
            MainScreenViewModel.MenuItem(
                id = IdThemeListRoute,
                title = getString(Res.string.main_themes_list_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdQPackListRoute,
                title = getString(Res.string.main_qpack_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdFavoritesRoute,
                title = getString(Res.string.main_favorites_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdViewsHistoryRoute,
                title = getString(Res.string.main_views_history_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdQPackSelectionRoute,
                title = getString(Res.string.main_qpack_selection_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdCourseListRoute,
                title = getString(Res.string.main_courses_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdSettingsRoute,
                title = getString(Res.string.main_settings_title).asAnnotated()
            ),
        )
    }
}