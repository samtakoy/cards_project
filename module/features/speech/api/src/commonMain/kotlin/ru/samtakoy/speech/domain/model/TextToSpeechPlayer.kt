package ru.samtakoy.speech.domain.model

interface TextToSpeechPlayer {
    suspend fun say(vararg text: String, pauseBetweenMs: Int = 0): TextToSpeechPlayerResult
    fun stop()
    fun close()
}