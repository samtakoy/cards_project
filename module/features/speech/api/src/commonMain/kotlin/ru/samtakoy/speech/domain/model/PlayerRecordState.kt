package ru.samtakoy.speech.domain.model

/**
 * @param cardNum номер карточки
 * @param isQuestion вопрос или ответ
 * @param textContent текущий текст: вопрос или ответ
 * */
data class PlayerRecordState(
    val cardNum: Int,
    val isQuestion: Boolean,
    val textContent: String
)