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
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.core.design_system.previewutils.getPreviewSelectableItems
import ru.samtakoy.presentation.utils.getALoremIpsum

@Preview(/*uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_NIGHT_YES*/)
@Composable
private fun MySelectableItem_Preview() = MyTheme {
    Column(
        modifier = Modifier.Companion
            .background(MyColors.getScreenBackground())
            .padding(MyOffsets.small),
        verticalArrangement = Arrangement.spacedBy(UiOffsets.listItemOffset)
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