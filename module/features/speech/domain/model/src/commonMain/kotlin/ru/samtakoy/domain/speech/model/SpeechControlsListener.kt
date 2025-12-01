package ru.samtakoy.domain.speech.model

/** Листенер прослушивания нажатия кнопок плейера */
interface SpeechControlsListener {
    fun pausePlayback()
    fun resumePlayback()
    fun nextCard()
    fun previousCard()
    fun stopPlayback()
}