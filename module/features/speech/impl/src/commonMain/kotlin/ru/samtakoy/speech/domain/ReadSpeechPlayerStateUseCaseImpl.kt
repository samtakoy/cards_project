package ru.samtakoy.speech.domain

import kotlinx.coroutines.flow.StateFlow
import ru.samtakoy.speech.domain.model.SpeechPlayerState
import ru.samtakoy.speech.domain.repo.PlayerStateRepository

internal class ReadSpeechPlayerStateUseCaseImpl(
    private val playerStateRepository: PlayerStateRepository
) : ReadSpeechPlayerStateUseCase {

    override fun observePlaybackState(): StateFlow<SpeechPlayerState> {
        return playerStateRepository.getPlaybackStateAsFlow()
    }
}