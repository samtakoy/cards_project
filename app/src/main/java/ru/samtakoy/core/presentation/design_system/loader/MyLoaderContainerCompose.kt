package ru.samtakoy.core.presentation.design_system.loader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import ru.samtakoy.core.presentation.design_system.base.MyColors

@Composable
fun MyLoaderContainer(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .background(color = MyColors.backgroundLoaderBgColor)
                .pointerInput(Unit) {}
        ) {
            // пока пусто
        }
    }
}

