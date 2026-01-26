package ru.samtakoy.tabnavigation.presentation.utils

import androidx.navigation.NavHostController
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute

fun NavHostController.changeTab(route: MainTabRoute) {
    navigate(route) {
        popUpTo(graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
