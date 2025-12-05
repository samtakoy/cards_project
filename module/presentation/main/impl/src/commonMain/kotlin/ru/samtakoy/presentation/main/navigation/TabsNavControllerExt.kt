package ru.samtakoy.presentation.main.navigation

import androidx.navigation.NavHostController
import ru.samtakoy.presentation.navigation.MainTabRoute

fun NavHostController.changeTab(route: MainTabRoute) {
    navigate(route) {
        popUpTo(graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
