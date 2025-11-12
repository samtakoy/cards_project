package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.MyColors
import ru.samtakoy.presentation.core.design_system.base.MyOffsets
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.round.MyFabButtonIcon
import ru.samtakoy.presentation.core.design_system.button.round.MyFabButtonUiModel
import ru.samtakoy.presentation.core.design_system.button.round.MyFabButtonView

@Preview
@Composable
private fun MyFabButtonView_Preview() = MyTheme {
    val models = listOf(
        MyFabButtonUiModel(
            id = AnyUiId(),
            icon = MyFabButtonIcon.Revert
        )
    ).toImmutableList()
    Column(
        modifier = Modifier
            .background(MyColors.getScreenBackground())
            .padding(MyOffsets.small),
        verticalArrangement = Arrangement.spacedBy(MyOffsets.small)
    ) {
        models.forEach {
            MyFabButtonView(model = it, onClick = {})
        }
    }
}
