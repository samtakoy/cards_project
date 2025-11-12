package ru.samtakoy.presentation.core.appelements.themelistitem

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.UiRadiuses
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape

@Composable
fun ThemeListItemView(
    model: ThemeListItemUiModel,
    modifier: Modifier = Modifier,
    onClick: ((id: LongUiId) -> Unit)? = null
) {
    ThemeListItemView(
        id = model.id,
        title = model.title,
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
fun ThemeListItemView(
    id: LongUiId,
    title: AnnotatedString,
    modifier: Modifier = Modifier,
    onClick: ((id: LongUiId) -> Unit)? = null,
    onLongClick: ((id: LongUiId) -> Unit)? = null
) {
    Box(
        modifier = modifier
            .background(
                color = MyColors.getListItemBgColor(),
                shape = getRoundedShape(UiRadiuses.listItemBg)
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
                .padding(horizontal = UiOffsets.listItemContentHPadding, vertical = UiOffsets.listItemContentVPadding)
                .align(Alignment.TopStart),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}