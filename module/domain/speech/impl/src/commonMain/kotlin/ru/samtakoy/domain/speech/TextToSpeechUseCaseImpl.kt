package ru.samtakoy.domain.speech

import ru.samtakoy.data.speech.TextToSpeechRepository
import ru.samtakoy.domain.speech.model.TextToSpeechPlayer

internal class TextToSpeechUseCaseImpl(
    private val repository: TextToSpeechRepository
) : TextToSpeechUseCase {

    override suspend fun getNewPlayer(): TextToSpeechPlayer? {
        return repository.createPlayer()
    }
}