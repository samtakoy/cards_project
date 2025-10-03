package ru.samtakoy.core.presentation.design_system.list_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.samtakoy.core.presentation.design_system.base.MyColors

@Composable
fun ListItemContainer(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(color = resolveColor(index % 2))
    ) {
        content()
    }
}

@ReadOnlyComposable
@Composable
fun resolveColor(index: Int): Color {
    return if (index % 2 == 0) {
        MyColors.getListItemOddBgColor()
    } else {
        MyColors.getListItemEvenBgColor()
    }
}
