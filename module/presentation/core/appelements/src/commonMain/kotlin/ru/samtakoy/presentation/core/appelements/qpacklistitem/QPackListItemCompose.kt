package ru.samtakoy.presentation.core.appelements.qpacklistitem

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.MySizes
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
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
        viewCount = model.viewCount,
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
    viewCount: AnnotatedString? = null,
    onClick: ((id: LongUiId) -> Unit)? = null,
    onLongClick: ((id: LongUiId) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(getRoundedShape(UiRadiuses.listItemBg))
            .background(
                color = MyColors.getListItemBgLightColor()
            )
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
                .background(color = CardTopBackgroundColor)
                .padding(horizontal = UiOffsets.listItemContentHPadding)
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = UiOffsets.listItemContentHPadding),
            horizontalArrangement = Arrangement.spacedBy(MyOffsets.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = creationDate,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
            viewCount?.let {
                Icon(
                    imageVector = Icons.Outlined.Face,
                    contentDescription = null,
                    modifier = Modifier.size(MySizes.smallIcon),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = viewCount,
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private val CardTopBackgroundColor = Color.Blue.copy(alpha = 0.055f)