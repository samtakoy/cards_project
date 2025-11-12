package ru.samtakoy.presentation.core.design_system.previewutils

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemUiModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.getALoremIpsum

@Stable
internal fun getMyRadioPreviewItems(): ImmutableList<MyRadioItemUiModel> = listOf(
    MyRadioItemUiModel(
        id = AnyUiId(),
        text = "item 1".asA(),
        isSelected = false
    ),
    MyRadioItemUiModel(
        id = AnyUiId(),
        text = "item1".asA(),
        isSelected = true
    ),
    MyRadioItemUiModel(
        id = AnyUiId(),
        text = getALoremIpsum(10),
        isSelected = false
    ),
    MyRadioItemUiModel(
        id = AnyUiId(),
        text = getALoremIpsum(),
        isSelected = false
    ),
).toImmutableList()