package ru.samtakoy.presentation.themes.list

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute

@Immutable
@Serializable
data class ThemeListRoute(
    val themeId: Long,
    val themeTitle: String?
) : MainTabRoute