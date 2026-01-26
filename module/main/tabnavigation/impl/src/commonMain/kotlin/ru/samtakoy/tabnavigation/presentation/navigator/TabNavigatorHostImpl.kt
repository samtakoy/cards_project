package ru.samtakoy.tabnavigation.presentation.navigator

import kotlinx.coroutines.flow.Flow

internal class TabNavigatorHostImpl(
    private val tabNavigatorImpl: TabNavigatorImpl
) : TabNavigatorHost {

    override fun getNavigationEventsAsFlow(): Flow<TabNavigatorEvent> {
        return tabNavigatorImpl.events
    }

    override fun getTabNavigator(): TabNavigator {
        return tabNavigatorImpl
    }
}