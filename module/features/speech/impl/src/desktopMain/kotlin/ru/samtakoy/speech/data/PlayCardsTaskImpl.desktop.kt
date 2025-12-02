package ru.samtakoy.speech.data

import ru.samtakoy.speech.domain.PlayCardsTask

internal class PlayCardsTaskImpl()  : PlayCardsTask {
    override suspend fun start(cardIds: List<Long>, onlyQuestions: Boolean) {
        // Not implemented yet
    }

    override fun stop() {
        // Not implemented yet
    }
}