package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.previewutils.getMyRadioPreviewItems
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemView

@Preview
@Composable
private fun MyRadioItemView_Preview() = MyTheme {
    Column(
        modifier = Modifier.Companion
            .background(MyTheme.colors.screenBackground)
            .padding(MyTheme.offsets.listItemOffset),
        verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.itemsSmallVOffset)
    ) {
        getMyRadioPreviewItems().forEach {
            MyRadioItemView(
                model = it,
                onClick = {}
            )
        }
    }
}