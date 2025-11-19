package ru.samtakoy.domain.speech

import ru.samtakoy.domain.speech.model.TextToSpeechPlayer

interface TextToSpeechUseCase {
    suspend fun getNewPlayer(): TextToSpeechPlayer?
}