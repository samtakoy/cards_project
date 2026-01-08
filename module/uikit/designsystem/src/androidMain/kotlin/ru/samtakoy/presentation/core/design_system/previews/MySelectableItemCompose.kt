package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.previewutils.getPreviewSelectableItems
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.utils.getALoremIpsum

@Preview(/*uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_NIGHT_YES*/)
@Composable
private fun MySelectableItem_Preview() = MyTheme {
    Column(
        modifier = Modifier.Companion
            .background(MyTheme.colors.screenBackground)
            .padding(MyTheme.offsets.itemsSmallVOffset),
        verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.listItemOffset)
    ) {
        getPreviewSelectableItems().forEach {
            MySelectableItem(
                model = it,
                onClick = null
            )
        }
        MySelectableItem(
            model = MySelectableItemModel(AnyUiId(), getALoremIpsum(12), true, true),
            onClick = null,
            maxLines = 1
        )
    }
}