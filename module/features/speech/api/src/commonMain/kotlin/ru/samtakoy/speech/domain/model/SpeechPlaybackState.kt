package ru.samtakoy.speech.domain.model

/**
 * @param title заголовок для отображения текстом
 * @param description описание для отображения текстом
 * @param isPaused playing/paused
 * @param audio текста для воспроизведения
 * @param isQuestion вопрос или ответ
 * */
data class SpeechPlaybackState(
    val currentCardNum: Int,
    val totalCards: Int,
    val isPaused: Boolean,
    val title: String,
    val description: String,
    val audio: List<String>,
    val isQuestion: Boolean
)