package ru.samtakoy.navigation.presentation

import androidx.compose.runtime.Immutable

@Immutable
interface RootNavigator {
    fun goBack()
    fun replace(route: RootRoute)
    fun navigate(route: RootRoute)
}