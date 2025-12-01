package ru.samtakoy.domain.speech.model

sealed interface TextToSpeechPlayerResult {
    object NotInited : TextToSpeechPlayerResult
    object Succeed : TextToSpeechPlayerResult
    object Canceled : TextToSpeechPlayerResult
    class Failed(val t: Throwable) : TextToSpeechPlayerResult
}