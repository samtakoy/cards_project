package ru.samtakoy.presentation.core.design_system.scrollbar.vertical

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformVerticalScrollbar(
    scrollState: LazyListState,
    modifier: Modifier = Modifier
)

@Composable
expect fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
)