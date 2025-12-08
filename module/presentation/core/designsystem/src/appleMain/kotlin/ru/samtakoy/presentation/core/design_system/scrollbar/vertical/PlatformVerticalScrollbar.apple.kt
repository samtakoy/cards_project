package ru.samtakoy.presentation.core.design_system.scrollbar.vertical

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    modifier: androidx.compose.ui.Modifier
) {
    // только desktop
}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    // только desktop
}
