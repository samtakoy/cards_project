package ru.samtakoy.platform.speech

interface PlayCardsTask {
    suspend fun start(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    )
    fun stop()
}