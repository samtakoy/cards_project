package ru.samtakoy.platform.speech

import ru.samtakoy.domain.speech.PlayCardsTask

internal class PlayCardsTaskImpl()  : PlayCardsTask {
    override suspend fun start(cardIds: List<Long>, onlyQuestions: Boolean) {
        // Not implemented yet
    }

    override fun stop() {
        // Not implemented yet
    }
}