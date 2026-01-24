package ru.samtakoy.presentation.core.design_system.selectable_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme

@Composable
fun MySelectableItem(
    model: MySelectableItemModel,
    onClick: ((MySelectableItemModel) -> Unit)?,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    val updatedModel = rememberUpdatedState(model)
    val updatedOnClick = rememberUpdatedState(onClick)

    MySelectableItem(
        text = model.text,
        isChecked = model.isChecked,
        isEnabled = model.isEnabled,
        onClick = remember {
            { updatedOnClick.value?.invoke(updatedModel.value) }
        },
        modifier = modifier,
        maxLines = maxLines,
        contentDescription = model.contentDescription
    )
}

@Composable
fun MySelectableItem(
    text: AnnotatedString,
    isChecked: Boolean,
    isEnabled: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    contentDescription: String? = null
) {

    Row(
        modifier = modifier
            .clickable(enabled = isEnabled) {
                onClick?.invoke()
            }
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription.orEmpty()
                role = Role.Checkbox
            },
        horizontalArrangement = Arrangement.spacedBy(MyTheme.offsets.itemsSmallHOffset, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null,
            modifier = Modifier.pointerInput(Unit) {}
        )

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
        )
    }
}