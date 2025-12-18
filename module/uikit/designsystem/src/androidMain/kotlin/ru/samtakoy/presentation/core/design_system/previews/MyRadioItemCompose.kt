package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemView
import ru.samtakoy.presentation.core.design_system.previewutils.getMyRadioPreviewItems

@Preview
@Composable
private fun MyRadioItemView_Preview() = MyTheme {
    Column(
        modifier = Modifier.Companion
            .background(MyColors.getScreenBackground())
            .padding(UiOffsets.listItemOffset),
        verticalArrangement = Arrangement.spacedBy(MyOffsets.small)
    ) {
        getMyRadioPreviewItems().forEach {
            MyRadioItemView(
                model = it,
                onClick = {}
            )
        }
    }
}