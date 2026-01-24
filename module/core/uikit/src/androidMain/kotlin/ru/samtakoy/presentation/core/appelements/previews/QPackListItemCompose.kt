package ru.samtakoy.presentation.core.appelements.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemUiModel
import ru.samtakoy.presentation.core.appelements.qpacklistitem.QPackListItemView
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.getALoremIpsum

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
            .background(MyTheme.colors.screenBackground)
            .padding(MyTheme.offsets.itemsSmallVOffset),
        verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.listItemOffset)
    ) {
        myItems.forEach {
            key(it.id) { QPackListItemView(model = it) }
        }
    }
}