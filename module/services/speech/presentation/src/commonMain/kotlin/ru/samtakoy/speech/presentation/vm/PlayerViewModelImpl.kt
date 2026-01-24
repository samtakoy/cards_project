package ru.samtakoy.speech.presentation.vm

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.model.UiId
import ru.samtakoy.presentation.core.design_system.player.MyPlayerUiModel
import ru.samtakoy.speech.domain.ReadSpeechPlayerStateUseCase
import ru.samtakoy.speech.domain.model.SpeechControlsListener
import ru.samtakoy.speech.domain.model.SpeechPlayerState
import ru.samtakoy.speech.presentation.mapper.PlayerUiStateMapper
import ru.samtakoy.speech.presentation.model.ControlId
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.Action
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.Event
import ru.samtakoy.speech.presentation.vm.PlayerViewModel.State

class PlayerViewModelImpl(
    private val playerStateReadUseCase: ReadSpeechPlayerStateUseCase,
    private val playerDispatcher: Dispatcher<SpeechControlsListener>,
    private val playerStateMapper: PlayerUiStateMapper,
    scopeProvider: ScopeProvider,
    private val clearCallback: () -> Unit
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        player = MyPlayerUiModel(playerStateMapper.mapInitial())
    )
), PlayerViewModel {

    init {
        subscribeData()
    }

    override fun onCleared() {
        super.onCleared()
        clearCallback()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ControlClick -> onUiControlClick(event.controlId)
        }
    }

    private fun onUiControlClick(id: UiId) {
        when (id as? ControlId) {
            ControlId.Stop -> onUiStopClick()
            ControlId.Play -> onUiPlayClick()
            ControlId.Pause -> onUiPauseClick()
            ControlId.Prev -> onUiPrevClick()
            ControlId.Next -> onUiNextClick()
            null -> Unit
        }
    }

    private fun onUiStopClick() {
        playerDispatcher.dispatch { stopPlayback() }
    }

    private fun onUiPlayClick() {
        playerDispatcher.dispatch { resumePlayback() }
    }

    private fun onUiPauseClick() {
        playerDispatcher.dispatch { pausePlayback() }
    }

    private fun onUiPrevClick() {
        playerDispatcher.dispatch { previousCard() }
    }

    private fun onUiNextClick() {
        playerDispatcher.dispatch { nextCard() }
    }

    private fun subscribeData() {
        playerStateReadUseCase.observePlaybackState()
            .onEach { state ->
                when (state) {
                    is SpeechPlayerState.Active -> updatePlayer(state)
                    SpeechPlayerState.Disabled -> disablePlayer()
                }
            }
            .launchIn(mainScope)
    }

    private suspend fun updatePlayer(state: SpeechPlayerState.Active) {
        updateState { uiState ->
            uiState.copy(
                player = uiState.player.copy(
                    state = playerStateMapper.mapActive(state)
                )
            )
        }
    }

    private suspend fun disablePlayer() {
        updateState { uiState ->
            uiState.copy(
                player = uiState.player.copy(
                    state = MyPlayerUiModel.State.Invisible
                )
            )
        }
    }
}