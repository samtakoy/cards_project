package ru.samtakoy.presentation.main.mapper

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.courses.CourseListRoute
import ru.samtakoy.presentation.favorites.FavoritesRoute
import ru.samtakoy.presentation.main.impl.R
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdCourseListRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdFavoritesRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdQPackListRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdQPackSelectionRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdSettingsRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdThemeListRoute
import ru.samtakoy.presentation.main.mapper.MainScreenContentMapper.Companion.IdViewsHistoryRoute
import ru.samtakoy.presentation.main.vm.MainScreenViewModel
import ru.samtakoy.presentation.qpacks.QPackListRoute
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute
import ru.samtakoy.presentation.settings.SettingsRoute
import ru.samtakoy.presentation.themes.list.ThemeListRoute
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.presentation.viewshistory.ViewsHistoryRoute

internal interface MainScreenContentMapper {
    fun mapMenuItems(): List<MainScreenViewModel.MenuItem>

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

internal class MainScreenContentMapperImpl(
    private val resources: Resources
): MainScreenContentMapper {
    override fun mapMenuItems(): List<MainScreenViewModel.MenuItem> {
        return listOf(
            MainScreenViewModel.MenuItem(
                id = IdThemeListRoute,
                title = resources.getString(R.string.feature_themes_list_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdQPackListRoute,
                title = resources.getString(R.string.feature_qpack_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdFavoritesRoute,
                title = resources.getString(R.string.feature_favorites_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdViewsHistoryRoute,
                title = resources.getString(R.string.feature_views_history_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdQPackSelectionRoute,
                title = resources.getString(R.string.feature_qpack_selection_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdCourseListRoute,
                title = resources.getString(R.string.feature_courses_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = IdSettingsRoute,
                title = resources.getString(R.string.feature_settings_title).asAnnotated()
            ),
        )
    }
}