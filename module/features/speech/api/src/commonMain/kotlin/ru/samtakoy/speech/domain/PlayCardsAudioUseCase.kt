package ru.samtakoy.speech.domain

import kotlinx.coroutines.flow.StateFlow
import ru.samtakoy.speech.domain.model.SpeechPlayerState

interface PlayCardsAudioUseCase {
    /** Проигрывает карточки на плейере */
    suspend fun playCards(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    )
}