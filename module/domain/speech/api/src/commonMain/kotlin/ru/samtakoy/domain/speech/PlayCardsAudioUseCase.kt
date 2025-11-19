package ru.samtakoy.domain.speech

import kotlinx.coroutines.flow.StateFlow
import ru.samtakoy.domain.speech.model.SpeechPlaybackState

interface PlayCardsAudioUseCase {
    suspend fun playCards(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    )
    fun observePlaybackState(): StateFlow<SpeechPlaybackState?>
}