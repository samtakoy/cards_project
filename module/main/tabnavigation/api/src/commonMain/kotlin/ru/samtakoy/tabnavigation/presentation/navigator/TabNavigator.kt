package ru.samtakoy.tabnavigation.presentation.navigator

import androidx.compose.runtime.Immutable
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute

@Immutable
interface TabNavigator {
    fun showMainDrawer()
    fun putNextInStack(route: MainTabRoute)
}