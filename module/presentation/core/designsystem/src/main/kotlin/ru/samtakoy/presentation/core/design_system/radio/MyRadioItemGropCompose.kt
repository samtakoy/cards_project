package ru.samtakoy.presentation.core.design_system.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme

@Composable
fun MyRadioItemGropView(
    items: MutableState<ImmutableList<MyRadioItemUiModel>>,
    onNewSelect: (MyRadioItemUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val updatedOnSelect = rememberUpdatedState(onNewSelect)

    Column(
        modifier = modifier
            .padding(UiOffsets.listItemOffset),
        verticalArrangement = Arrangement.spacedBy(MyOffsets.small)
    ) {
        items.value.forEach {
            key(it.id) {
                MyRadioItemView(
                    model = it,
                    onClick = {
                        if (!it.isSelected) {
                            items.value = items.value.copyWithSelected(it.id)
                            updatedOnSelect.value(it)
                        }
                    }
                )
            }
        }
    }
}

@Stable
private fun List<MyRadioItemUiModel>.copyWithSelected(id: UiId): ImmutableList<MyRadioItemUiModel> {
    return this.map {
        val isSelected = it.id == id
        if (isSelected != it.isSelected) {
            it.copy(isSelected = isSelected)
        } else {
            it
        }
    }.toImmutableList()
}

@Preview
@Composable
private fun MyRadioItemGropView_Preview() = MyTheme {
    MyRadioItemGropView(
        items = remember { mutableStateOf(getMyRadioPreviewItems()) },
        onNewSelect = {}
    )
}