package ru.samtakoy.speech.platform

import ru.samtakoy.speech.domain.RunPlayCardsTaskUseCase

internal class RunPlayCardsTaskUseCaseImpl()  : RunPlayCardsTaskUseCase {
    override suspend fun start(cardIds: List<Long>, onlyQuestions: Boolean) {
        // Not implemented yet
    }

    override fun stop() {
        // Not implemented yet
    }
}