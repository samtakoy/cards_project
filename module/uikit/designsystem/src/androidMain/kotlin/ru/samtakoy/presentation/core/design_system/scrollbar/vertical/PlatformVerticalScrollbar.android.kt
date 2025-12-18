package ru.samtakoy.presentation.core.design_system.scrollbar.vertical

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: LazyListState,
    modifier: Modifier
) {
    // На Android не показываем Scrollbar
}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    // На Android не показываем Scrollbar
}