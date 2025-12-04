package ru.samtakoy.speech.domain.model

/**
 * Состояние плейера
 * */
sealed interface SpeechPlayerState {
    /** Остановлен */
    object Disabled : SpeechPlayerState
    /** Проигрывает карточки
     * @param cardIds карточки для проигрывания
     * @param curRecord данные текущего куска проигрывания
     * @param isPaused остановлен
     * @param onlyQuestions проигрывать только вопросы
     * */
    data class Active(
        val cardIds: List<Long>,
        val curRecord: PlayerRecordState,
        val isPaused: Boolean,
        val onlyQuestions: Boolean
    ) : SpeechPlayerState
}