package ru.samtakoy.presentation.core.design_system.button.round

import androidx.compose.runtime.Immutable
import ru.samtakoy.presentation.core.design_system.base.model.UiId

@Immutable
data class MyRoundButtonUiModel(
    val id: UiId,
    val icon: MyRoundButtonIcon,
    val size: MyRoundButtonSize
)

enum class MyRoundButtonIcon {
    Revert,
    MediaPlay,
    MediaStop,
    MediaPause,
    MediaPrev,
    MediaNext
}

enum class MyRoundButtonSize {
    Auto,
    ExtraSmall,
    Small,
    Medium,
    Large
}
