package ru.samtakoy.presentation.core.appelements.qpacklistitem

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.UiRadiuses
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape

@Composable
fun QPackListItemView(
    model: QPackListItemUiModel,
    modifier: Modifier = Modifier,
    onClick: ((id: LongUiId) -> Unit)? = null
) {
    QPackListItemView(
        id = model.id,
        title = model.title,
        creationDate = model.creationDate,
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
fun QPackListItemView(
    id: LongUiId,
    title: AnnotatedString,
    creationDate: AnnotatedString,
    modifier: Modifier = Modifier,
    onClick: ((id: LongUiId) -> Unit)? = null,
    onLongClick: ((id: LongUiId) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .background(
                color = MyColors.getListItemBgLightColor()
            )
            .clip(getRoundedShape(UiRadiuses.listItemBg))
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onLongClick = onLongClick?.let {
                    { onLongClick(id) }
                }
            ) {
                onClick?.invoke(id)
            }
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = creationDate,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}