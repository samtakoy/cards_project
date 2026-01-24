package ru.samtakoy.speech.data.repo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.samtakoy.speech.domain.model.PlayerRecordState
import ru.samtakoy.speech.domain.model.SpeechPlayerState
import ru.samtakoy.speech.domain.repo.PlayerStateRepository

internal class PlayerStateRepositoryImpl : PlayerStateRepository {
    private val playbackState = MutableStateFlow<SpeechPlayerState>(SpeechPlayerState.Disabled)

    override fun getPlaybackStateAsFlow(): StateFlow<SpeechPlayerState> {
        return playbackState
    }

    override fun activate(
        cardIds: List<Long>,
        curRecord: PlayerRecordState,
        isPaused: Boolean,
        onlyQuestions: Boolean
    ) {
        playbackState.value = SpeechPlayerState.Active(
            cardIds = cardIds,
            curRecord = curRecord,
            isPaused = isPaused,
            onlyQuestions = onlyQuestions
        )
    }

    override fun disable() {
        playbackState.value = SpeechPlayerState.Disabled
    }

    override fun updateRecordStateIfActive(newRecord: PlayerRecordState) {
        playbackState.update { currentState ->
            if (currentState is SpeechPlayerState.Active) {
                currentState.copy(
                    curRecord = newRecord
                )
            } else {
                currentState
            }
        }
    }

    override fun updatePlayingIfActive(isPaused: Boolean) {
        playbackState.update { currentState ->
            if (currentState is SpeechPlayerState.Active) {
                currentState.copy(
                    isPaused = isPaused
                )
            } else {
                currentState
            }
        }
    }
}