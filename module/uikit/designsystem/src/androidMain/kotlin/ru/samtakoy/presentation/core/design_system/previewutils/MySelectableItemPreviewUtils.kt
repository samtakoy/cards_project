package ru.samtakoy.presentation.core.design_system.previewutils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.utils.getLoremIpsum

fun getPreviewSelectableItems(): ImmutableList<MySelectableItemModel> {
    return listOf(
        MySelectableItemModel(
            id = LongUiId(1L),
            text = getLoremIpsum().asAnnotated(),
            isChecked = true,
            isEnabled = false
        ),
        MySelectableItemModel(
            id = LongUiId(1L),
            text = getLoremIpsum(12).asAnnotated(),
            isChecked = false,
            isEnabled = true
        ),
        MySelectableItemModel(
            id = LongUiId(1L),
            text = getLoremIpsum(10).asAnnotated(),
            isChecked = false,
            isEnabled = true
        )
    ).toImmutableList()
}