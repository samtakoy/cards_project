package ru.samtakoy.speech.presentation.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonIcon
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonSize
import ru.samtakoy.presentation.core.design_system.button.round.MyRoundButtonUiModel
import ru.samtakoy.presentation.core.design_system.player.MyPlayerUiModel
import ru.samtakoy.speech.domain.model.SpeechPlayerState
import ru.samtakoy.speech.presentation.model.ControlId

interface PlayerUiStateMapper {
    fun mapInitial(): MyPlayerUiModel.State
    fun mapActive(state: SpeechPlayerState.Active): MyPlayerUiModel.State.Visible
}

internal class PlayerUiStateMapperImpl : PlayerUiStateMapper {
    override fun mapInitial(): MyPlayerUiModel.State {
        return MyPlayerUiModel.State.Invisible
    }

    override fun mapActive(state: SpeechPlayerState.Active): MyPlayerUiModel.State.Visible {
        return MyPlayerUiModel.State.Visible(
            isPlaying = state.isPaused.not(),
            recordsCount = state.cardIds.size,
            currentIndex = state.curRecord.cardNum - 1,
            controls = mapControls(state.isPaused).toImmutableList()
        )
    }

    private fun mapControls(isPaused: Boolean): List<MyRoundButtonUiModel> {
        return listOf(
            stopBtn,
            prevBtn,
            if (isPaused) playBtn else pauseBtn,
            NextBtn
        )
    }

    private val stopBtn: MyRoundButtonUiModel by lazy {
        MyRoundButtonUiModel(
            id = ControlId.Stop,
            icon = MyRoundButtonIcon.MediaStop,
            size = MyRoundButtonSize.Medium
        )
    }

    private val playBtn: MyRoundButtonUiModel by lazy {
        MyRoundButtonUiModel(
            id = ControlId.Play,
            icon = MyRoundButtonIcon.MediaPlay,
            size = MyRoundButtonSize.Medium
        )
    }

    private val pauseBtn: MyRoundButtonUiModel by lazy {
        MyRoundButtonUiModel(
            id = ControlId.Pause,
            icon = MyRoundButtonIcon.MediaPause,
            size = MyRoundButtonSize.Medium
        )
    }
    private val prevBtn: MyRoundButtonUiModel by lazy {
        MyRoundButtonUiModel(
            id = ControlId.Prev,
            icon = MyRoundButtonIcon.MediaPrev,
            size = MyRoundButtonSize.Medium
        )
    }

    private val NextBtn: MyRoundButtonUiModel by lazy {
        MyRoundButtonUiModel(
            id = ControlId.Next,
            icon = MyRoundButtonIcon.MediaNext,
            size = MyRoundButtonSize.Medium
        )
    }
}