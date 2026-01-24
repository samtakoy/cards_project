package ru.samtakoy.speech.domain.repo

import kotlinx.coroutines.flow.StateFlow
import ru.samtakoy.speech.domain.model.PlayerRecordState
import ru.samtakoy.speech.domain.model.SpeechPlayerState

/** Состояние плейера */
internal interface PlayerStateRepository {
    fun getPlaybackStateAsFlow(): StateFlow<SpeechPlayerState>
    fun activate(
        cardIds: List<Long>,
        curRecord: PlayerRecordState,
        isPaused: Boolean,
        onlyQuestions: Boolean
    )
    fun disable()
    fun updateRecordStateIfActive(newRecord: PlayerRecordState)
    fun updatePlayingIfActive(isPaused: Boolean)
}