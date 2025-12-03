package ru.samtakoy.presentation.core.design_system.player

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonUiModel

@Immutable
data class MyPlayerUiModel(
    val state: State
) {
    @Immutable
    sealed interface State {
        @Immutable
        object Invisible : State
        @Immutable
        data class Visible(
            val isPlaying: Boolean,
            val recordsCount: Int,
            val currentIndex: Int,
            val controls: ImmutableList<MyRoundButtonUiModel>
        ) : State
    }
}