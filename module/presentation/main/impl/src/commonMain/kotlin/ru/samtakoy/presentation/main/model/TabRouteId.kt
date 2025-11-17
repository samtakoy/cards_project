package ru.samtakoy.presentation.main.model

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
internal enum class TabRouteId : UiId {
    IdThemeListRoute,
    IdQPackListRoute,
    IdFavoritesRoute,
    IdViewsHistoryRoute,
    IdQPackSelectionRoute,
    IdCourseListRoute,
    IdSettingsRoute
}