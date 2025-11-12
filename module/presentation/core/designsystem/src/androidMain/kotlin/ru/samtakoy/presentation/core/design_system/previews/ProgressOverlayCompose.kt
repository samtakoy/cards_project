package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.progress.ProgressOverlayView
import ru.samtakoy.presentation.utils.asA

@Preview
@Composable
private fun ProgressOverlayView_Preview() = MyTheme {
    ProgressOverlayView(
        title = "title".asA(),
        subtitle = "subtitle".asA()
    )
}