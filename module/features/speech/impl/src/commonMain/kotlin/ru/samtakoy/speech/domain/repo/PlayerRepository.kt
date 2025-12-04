package ru.samtakoy.speech.domain.repo

import ru.samtakoy.speech.domain.model.TextToSpeechPlayer

interface PlayerRepository {
    suspend fun getPlayer(): TextToSpeechPlayer?
}