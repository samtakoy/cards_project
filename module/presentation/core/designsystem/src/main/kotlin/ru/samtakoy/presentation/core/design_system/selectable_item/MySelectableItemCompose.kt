package ru.samtakoy.presentation.core.design_system.selectable_item

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.utils.getALoremIpsum

@Composable
fun MySelectableItem(
    model: MySelectableItemModel,
    onClick: ((MySelectableItemModel) -> Unit)?,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    val updatedModel = rememberUpdatedState(model)
    val updatedOnClick = rememberUpdatedState(onClick)
    Row(
        modifier = modifier
            .clickable(enabled = model.isEnabled) {
                updatedOnClick.value?.invoke(updatedModel.value)
            }
            .semantics(mergeDescendants = true) {
                contentDescription = model.contentDescription.orEmpty()
                role = Role.Checkbox
            },
        horizontalArrangement = Arrangement.spacedBy(MyOffsets.small, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = model.isChecked,
            onCheckedChange = null,
            modifier = Modifier.pointerInput(Unit) {}
        )

        Text(
            text = model.text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_NIGHT_YES)
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