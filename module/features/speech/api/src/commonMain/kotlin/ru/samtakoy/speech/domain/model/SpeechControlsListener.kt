package ru.samtakoy.speech.domain.model

/** Листенер прослушивания нажатия кнопок плейера */
interface SpeechControlsListener {
    fun pausePlayback()
    fun resumePlayback()
    fun nextCard()
    fun previousCard()
    fun stopPlayback()
}