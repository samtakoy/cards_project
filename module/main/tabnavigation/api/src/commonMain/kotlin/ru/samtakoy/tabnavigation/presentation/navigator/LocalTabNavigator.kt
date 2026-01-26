package ru.samtakoy.tabnavigation.presentation.navigator

import androidx.compose.runtime.staticCompositionLocalOf

val LocalTabNavigator = staticCompositionLocalOf<TabNavigator> {
    error("No TabNavigator provided")
}