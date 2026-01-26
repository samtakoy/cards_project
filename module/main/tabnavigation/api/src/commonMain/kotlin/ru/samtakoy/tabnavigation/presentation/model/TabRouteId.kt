package ru.samtakoy.tabnavigation.presentation.model

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.base.model.UiId

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