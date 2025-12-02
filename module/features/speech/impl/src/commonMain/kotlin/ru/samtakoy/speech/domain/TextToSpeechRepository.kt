package ru.samtakoy.speech.domain

import ru.samtakoy.speech.domain.model.TextToSpeechPlayer

interface TextToSpeechRepository {
    suspend fun getPlayer(): TextToSpeechPlayer?
}