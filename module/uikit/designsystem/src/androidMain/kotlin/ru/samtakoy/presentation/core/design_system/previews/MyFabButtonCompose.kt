package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonIcon
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonSize
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonUiModel
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonView

@Preview
@Composable
private fun MyRoundButtonView_Preview() = MyTheme {
    val models = listOf(
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.Revert,
            size = MyRoundButtonSize.Large
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPlay,
            size = MyRoundButtonSize.Medium
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPause,
            size = MyRoundButtonSize.Medium
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPlay,
            size = MyRoundButtonSize.Small
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPause,
            size = MyRoundButtonSize.Small
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPlay,
            size = MyRoundButtonSize.ExtraSmall
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPause,
            size = MyRoundButtonSize.ExtraSmall
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaStop,
            size = MyRoundButtonSize.ExtraSmall
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPrev,
            size = MyRoundButtonSize.ExtraSmall
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaNext,
            size = MyRoundButtonSize.ExtraSmall
        )

    ).toImmutableList()
    Column(
        modifier = Modifier
            .background(MyTheme.colors.screenBackground)
            .padding(
                horizontal = MyTheme.offsets.screenContentHPadding,
                vertical = MyTheme.offsets.screenContentVPadding
            ),
        verticalArrangement = Arrangement.spacedBy(MyTheme.offsets.itemsSmallVOffset)
    ) {
        models.forEach {
            MyRoundButtonView(model = it, onClick = {})
        }
    }
}
