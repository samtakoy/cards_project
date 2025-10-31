package ru.samtakoy.presentation.core.appelements.themelistitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.UiRadiuses
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.presentation.utils.getALoremIpsum

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

@Preview
@Composable
private fun ThemeListItemView_Preview() = MyTheme {
    val myItems = listOf<ThemeListItemUiModel>(
        ThemeListItemUiModel(id = LongUiId(1L), title = "Тема 1".asAnnotated()),
        ThemeListItemUiModel(id = LongUiId(2L), title = getALoremIpsum(8)),
        ThemeListItemUiModel(id = LongUiId(3L), title = "Тема 3".asAnnotated())
    )
    Column(
        modifier = Modifier
            .background(MyColors.getScreenBackground())
            .padding(MyOffsets.small),
        verticalArrangement = Arrangement.spacedBy(UiOffsets.listItemOffset)
    ) {
        myItems.forEach {
            key(it.id) { ThemeListItemView(model = it) }
        }
    }
}