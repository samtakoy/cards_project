package ru.samtakoy.presentation.core.design_system.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonIcon
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonSize
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonUiModel
import ru.samtakoy.presentation.core.design_system.player.MyPlayerUiModel
import ru.samtakoy.presentation.core.design_system.player.MyPlayerView

@Preview
@Composable
private fun MyPlayerView_Preview() = MyTheme {
    val controls = listOf(
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaStop,
            size = MyRoundButtonSize.Medium
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPrev,
            size = MyRoundButtonSize.Medium
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaPlay,
            size = MyRoundButtonSize.Medium
        ),
        MyRoundButtonUiModel(
            id = AnyUiId(),
            icon = MyRoundButtonIcon.MediaNext,
            size = MyRoundButtonSize.Medium
        ),
    ).toImmutableList()
    MyPlayerView(
        model = remember {
            MyPlayerUiModel(
                state = MyPlayerUiModel.State.Visible(
                    isPlaying = true,
                    recordsCount = 120,
                    currentIndex = 100,
                    controls = controls
                )
            )
        },
    )
}