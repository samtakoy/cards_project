package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemGropView
import ru.samtakoy.presentation.core.design_system.previewutils.getMyRadioPreviewItems

@Preview
@Composable
private fun MyRadioItemGropView_Preview() = MyTheme {
    MyRadioItemGropView(
        items = remember { mutableStateOf(getMyRadioPreviewItems()) },
        onNewSelect = {}
    )
}