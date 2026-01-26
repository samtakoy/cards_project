package ru.samtakoy.tabnavigation.presentation.navigator

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

@Immutable
interface TabNavigatorHost {
    fun getNavigationEventsAsFlow(): Flow<TabNavigatorEvent>
    fun getTabNavigator(): TabNavigator
}