package ru.samtakoy.presentation.main.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.presentation.main.model.TabRouteId
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
}

internal class MainScreenContentMapperImpl: MainScreenContentMapper {
    override suspend fun mapMenuItems(): List<MainScreenViewModel.MenuItem> {
        return listOf(
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdThemeListRoute,
                title = getString(Res.string.main_themes_list_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdQPackListRoute,
                title = getString(Res.string.main_qpack_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdFavoritesRoute,
                title = getString(Res.string.main_favorites_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdViewsHistoryRoute,
                title = getString(Res.string.main_views_history_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdQPackSelectionRoute,
                title = getString(Res.string.main_qpack_selection_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdCourseListRoute,
                title = getString(Res.string.main_courses_title).asAnnotated()
            ),
            MainScreenViewModel.MenuItem(
                id = TabRouteId.IdSettingsRoute,
                title = getString(Res.string.main_settings_title).asAnnotated()
            ),
        )
    }
}