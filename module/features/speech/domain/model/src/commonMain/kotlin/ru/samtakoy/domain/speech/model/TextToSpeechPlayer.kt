package ru.samtakoy.domain.speech.model

import androidx.compose.runtime.Immutable

@Immutable
interface TextToSpeechPlayer {
    suspend fun say(vararg text: String, pauseBetweenMs: Int = 0): TextToSpeechPlayerResult
    fun stop()
    fun close()
}