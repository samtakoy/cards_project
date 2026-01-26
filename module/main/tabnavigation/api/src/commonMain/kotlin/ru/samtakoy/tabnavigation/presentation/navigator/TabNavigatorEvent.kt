package ru.samtakoy.tabnavigation.presentation.navigator

import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute

sealed interface TabNavigatorEvent {
    object ShowMainDrawer : TabNavigatorEvent
    class PutNextScreenInStack(val route: MainTabRoute) : TabNavigatorEvent
}