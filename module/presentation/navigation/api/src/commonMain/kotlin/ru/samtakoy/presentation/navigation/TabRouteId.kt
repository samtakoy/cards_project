package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
enum class TabRouteId : UiId {
    ThemeList,
    QPackList,
    Favorites,
    ViewsHistory,
    QPackSelection,
    CourseList,
    Settings
}