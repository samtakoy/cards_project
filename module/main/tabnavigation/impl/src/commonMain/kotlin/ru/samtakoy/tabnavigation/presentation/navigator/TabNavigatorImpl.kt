package ru.samtakoy.tabnavigation.presentation.navigator

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute

internal class TabNavigatorImpl : TabNavigator {

    private val _events = MutableSharedFlow<TabNavigatorEvent>(
        replay = 0,
        extraBufferCapacity = EXTRA_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    val events: Flow<TabNavigatorEvent>
        get() = _events

    override fun showMainDrawer() {
        _events.tryEmit(TabNavigatorEvent.ShowMainDrawer)
    }

    override fun putNextInStack(route: MainTabRoute) {
        _events.tryEmit(TabNavigatorEvent.PutNextScreenInStack(route))
    }

    companion object {
        private const val EXTRA_BUFFER_CAPACITY = 3
    }
}