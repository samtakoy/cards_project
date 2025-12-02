package ru.samtakoy.speech.domain

import kotlinx.coroutines.flow.StateFlow
import ru.samtakoy.speech.domain.model.SpeechPlaybackState

interface PlayCardsAudioUseCase {
    /** Проигрывает карточки на плейере */
    suspend fun playCards(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    )
    // TODO это будет читаться из репозитория
    fun observePlaybackState(): StateFlow<SpeechPlaybackState?>
}