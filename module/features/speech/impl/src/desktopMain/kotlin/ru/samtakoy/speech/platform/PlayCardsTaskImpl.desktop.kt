package ru.samtakoy.speech.platform

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.speech.domain.PlayCardsAudioUseCase
import ru.samtakoy.speech.domain.RunPlayCardsTaskUseCase

internal class RunPlayCardsTaskUseCaseImpl(
    val playCardsAudioUseCase: PlayCardsAudioUseCase,
    private val scopeProvider: ScopeProvider
)  : RunPlayCardsTaskUseCase {
    private var runJob: Job? = null

    override suspend fun start(cardIds: List<Long>, onlyQuestions: Boolean) {
        stop()
        runJob = scopeProvider.ioScope.launch {
            playCardsAudioUseCase.playCards(
                cardIds = cardIds,
                onlyQuestions = onlyQuestions
            )
        }
    }

    override fun stop() {
        runJob?.let {
            runJob = null
            it.cancel()
        }
    }
}