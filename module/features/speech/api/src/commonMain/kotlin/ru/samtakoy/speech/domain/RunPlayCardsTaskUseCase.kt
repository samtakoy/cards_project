package ru.samtakoy.speech.domain

interface RunPlayCardsTaskUseCase {
    /**
     * Запускает процесс проигрывания карточек способом зависимым от платформы.
     * */
    suspend fun start(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    )
    fun stop()
}