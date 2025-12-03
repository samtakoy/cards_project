package ru.samtakoy.presentation.core.design_system.radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import ru.samtakoy.presentation.core.design_system.base.MyOffsets

@Composable
fun MyRadioItemView(
    model: MyRadioItemUiModel,
    onClick: (MyRadioItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    val updatedModel = rememberUpdatedState(model)
    val updatedOnClick = rememberUpdatedState(onClick)
    Row(
        modifier = modifier
            .clickable(enabled = model.isEnabled) {
                updatedOnClick.value.invoke(updatedModel.value)
            }
            .semantics(mergeDescendants = true) {
                contentDescription = model.contentDescription.orEmpty()
                role = Role.RadioButton
            },
        horizontalArrangement = Arrangement.spacedBy(MyOffsets.small, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = model.isSelected,
            onClick = null,
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