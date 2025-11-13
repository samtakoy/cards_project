package ru.samtakoy.presentation.core.design_system.scrollbar.vertical

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: LazyListState,
    modifier: Modifier
) {
    VerticalScrollbarImpl(modifier, rememberScrollbarAdapter(scrollState))
}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    VerticalScrollbarImpl(modifier, rememberScrollbarAdapter(scrollState))
}

@Composable
private fun VerticalScrollbarImpl(
    modifier: Modifier,
    scrollbarAdapter: ScrollbarAdapter
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = scrollbarAdapter,
        style = LocalScrollbarStyle.current.copy(
            thickness = 12.dp,
            shape = RoundedCornerShape(4.dp)
        )
    )
}