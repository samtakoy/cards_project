package ru.samtakoy.speech.domain.model

sealed interface TextToSpeechPlayerResult {
    object NotInited : TextToSpeechPlayerResult
    object Succeed : TextToSpeechPlayerResult
    object Canceled : TextToSpeechPlayerResult
    class Failed(val t: Throwable) : TextToSpeechPlayerResult
}