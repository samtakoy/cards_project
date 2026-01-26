package ru.samtakoy.navigation.presentation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalRootNavigator = staticCompositionLocalOf<RootNavigator> {
    error("No RootNavigator provided")
}