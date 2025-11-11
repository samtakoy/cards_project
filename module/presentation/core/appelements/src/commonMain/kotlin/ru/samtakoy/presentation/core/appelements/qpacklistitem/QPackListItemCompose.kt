package ru.samtakoy.presentation.core.appelements.qpacklistitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.UiRadiuses
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.base.utils.getRoundedShape
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.getALoremIpsum

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

@Preview
@Composable
private fun QPackListItemView_Preview() = MyTheme {
    val myItems = listOf<QPackListItemUiModel>(
        QPackListItemUiModel(id = LongUiId(1L), title = "Карточки".asA(), creationDate = "10.04.2024".asA()),
        QPackListItemUiModel(id = LongUiId(2L), title = getALoremIpsum(8), creationDate = "11.04.2024".asA()),
        QPackListItemUiModel(id = LongUiId(3L), title = "Koin Annotations".asA(), creationDate = "12.04.2024".asA())
    )
    Column(
        modifier = Modifier
            .background(MyColors.getScreenBackground())
            .padding(MyOffsets.small),
        verticalArrangement = Arrangement.spacedBy(UiOffsets.listItemOffset)
    ) {
        myItems.forEach {
            key(it.id) { QPackListItemView(model = it) }
        }
    }
}