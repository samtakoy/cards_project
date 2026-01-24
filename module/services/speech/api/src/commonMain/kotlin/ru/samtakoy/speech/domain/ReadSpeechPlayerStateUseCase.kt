package ru.samtakoy.speech.domain

import kotlinx.coroutines.flow.StateFlow
import ru.samtakoy.speech.domain.model.SpeechPlayerState

interface ReadSpeechPlayerStateUseCase {
    fun observePlaybackState(): StateFlow<SpeechPlayerState>
}