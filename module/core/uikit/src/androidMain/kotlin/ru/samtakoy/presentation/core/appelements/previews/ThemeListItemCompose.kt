package ru.samtakoy.presentation.core.appelements.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.appelements.themelistitem.ThemeListItemUiModel
import ru.samtakoy.presentation.core.appelements.themelistitem.ThemeListItemView
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.presentation.utils.getALoremIpsum

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
            .background(MyTheme.colors.screenBackground)
            .padding(MyTheme.offsets.itemsSmallVOffset),
        verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.listItemOffset)
    ) {
        myItems.forEach {
            key(it.id) { ThemeListItemView(model = it) }
        }
    }
}