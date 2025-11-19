package ru.samtakoy.data.speech

import ru.samtakoy.domain.speech.model.TextToSpeechPlayer

interface TextToSpeechRepository {
    suspend fun createPlayer(): TextToSpeechPlayer?
}