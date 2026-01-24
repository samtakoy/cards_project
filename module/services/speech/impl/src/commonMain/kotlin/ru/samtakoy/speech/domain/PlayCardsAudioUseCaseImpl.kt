package ru.samtakoy.speech.domain

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.domain.card.CardsRepository
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.speech.domain.mapper.PlayerAudioMapper
import ru.samtakoy.speech.domain.model.PlayerRecordState
import ru.samtakoy.speech.domain.model.SpeechControlsListener
import ru.samtakoy.speech.domain.model.SpeechPlayerState
import ru.samtakoy.speech.domain.model.TextToSpeechPlayer
import ru.samtakoy.speech.domain.model.TextToSpeechPlayerResult
import ru.samtakoy.speech.domain.repo.PlayerRepository
import ru.samtakoy.speech.domain.repo.PlayerStateRepository

internal class PlayCardsAudioUseCaseImpl(
    private val dispatcher: Dispatcher<SpeechControlsListener>,
    private val cardsRepository: CardsRepository,
    private val audioMapper: PlayerAudioMapper,
    private val playerRepository: PlayerRepository,
    private val playerStateRepository: PlayerStateRepository,
    private val scopeProvider: ScopeProvider
) : PlayCardsAudioUseCase {

    /** Job проигрывателя */
    private var playerJob: Job? = null
    /** Закешированные карточки из БД */
    private val cardsCache = mutableMapOf<Long, Card?>()
    /** Последний стейт проигрывания */
    private var currentActiveState: SpeechPlayerState.Active? = null

    private val controlsListener = object : SpeechControlsListener {
        override fun pausePlayback() = this@PlayCardsAudioUseCaseImpl.pausePlayback()
        override fun resumePlayback() = this@PlayCardsAudioUseCaseImpl.resumePlayback()
        override fun nextCard() = this@PlayCardsAudioUseCaseImpl.nextCard()
        override fun previousCard() = this@PlayCardsAudioUseCaseImpl.previousCard()
        override fun stopPlayback() = this@PlayCardsAudioUseCaseImpl.stopPlayback()
    }

    override suspend fun playCards(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    ) {
        stopPlayback()
        var firstCardIndex = -1
        for ((index, id) in cardIds.withIndex()) {
            if (resolveCard(id) != null) {
                firstCardIndex = index
                break
            }
        }
        if (firstCardIndex < 0) {
            // Карточек не найдено в БД
            return
        }

        val player = playerRepository.getPlayer() ?: return
        dispatcher.addListener(controlsListener)
        try {
            // Стартовое состояние
            playFirstQuestion(
                initialList = cardIds,
                firstCardIndex = firstCardIndex,
                onlyQuestions = onlyQuestions
            )

            // Слушаем состояние и при изменении проигрываем звук либо нет
            @OptIn(ExperimentalCoroutinesApi::class)
            playerJob = playerStateRepository.getPlaybackStateAsFlow()
                .filterNotNull()
                .distinctUntilChanged()
                .mapLatest { playerState ->
                    currentActiveState = playerState as? SpeechPlayerState.Active
                    if (playerState is SpeechPlayerState.Active) {
                        updatePlayerByState(
                            state = playerState,
                            player = player,
                            onPlayingEnd = {
                                if (playNext(playerState).not()) {
                                    // последняя карта
                                    stopPlayback()
                                }
                            }
                        )
                    } else {
                        // Завершение
                        stopPlayback()
                    }
                    playerState
                }
                .launchIn(scopeProvider.ioScope)
            playerJob?.join()
        } catch (_: CancellationException) {
            // do nothing
        } catch (e: Throwable) {
            Napier.e(throwable = e) { "PlayCardsAudioUseCaseImpl playCards error" }
            // TODO пока так - хотим видеть проблему
            throw e
        } finally {
            playerStateRepository.disable()
            dispatcher.removeListener(controlsListener)
            player.close()
        }
    }

    /**
     * Проиграть текущую карточку либо остановить проигрывание в зависимости от [state].
     * @param state состояние проигрывания
     * @param player проигрыватель
     * @param onPlayingEnd вызывается после успешного проигрывания
     * */
    private suspend fun updatePlayerByState(
        state: SpeechPlayerState.Active,
        player: TextToSpeechPlayer,
        onPlayingEnd: suspend () -> Unit
    ) {
        if (state.isPaused) {
            player.stop()
        } else {
            player.stop()
            val audio = audioMapper.mapCardAudio(
                state = state,
                onlyQuestions = state.onlyQuestions
            ).toTypedArray()
            // Произносим
            val result = player.say(*audio)
            when (result) {
                TextToSpeechPlayerResult.Canceled -> Unit // ничего не делаем - внешнее воздействие
                TextToSpeechPlayerResult.NotInited, // TODO
                is TextToSpeechPlayerResult.Failed, // TODO если упали - на десктопе надо пробовать пересоздать плейер
                TextToSpeechPlayerResult.Succeed -> {
                    // если не отменено пользователем, то пробуем дальше
                    onPlayingEnd()
                }
            }
        }
    }

    /**
     * Первый вопрос.
     * Проверки на наличие карточки проведены до вызова.
     * */
    private suspend fun playFirstQuestion(
        initialList: List<Long>,
        firstCardIndex: Int,
        onlyQuestions: Boolean
    ) {
        val recordInfo = PlayerRecordState(
            cardNum = 1,
            isQuestion = true,
            textContent = resolveCard(initialList[firstCardIndex])?.question.orEmpty()
        )
        playerStateRepository.activate(
            cardIds = initialList,
            curRecord = recordInfo,
            isPaused = false,
            onlyQuestions = onlyQuestions
        )
    }

    /**
     * @param toForward направление перебора карточек "вперед"
     * @return false если не удалось найти ни обной карточки наачиная с cardNum в заданном направлении
     * */
    private suspend fun playQuestion(cardNum: Int, toForward: Boolean): Boolean {
        val cardInfo = resolveCardPosition(cardIndex = cardNum - 1, toForward = toForward) ?: return false
        playerStateRepository.updateRecordStateIfActive(
            PlayerRecordState(
                cardNum = cardInfo.cardIndex + 1,
                isQuestion = true,
                textContent = cardInfo.card.question
            )
        )
        return true
    }

    /**
     * @param toForward направление перебора карточек "вперед"
     * @return false если не удалось найти ни обной карточки наачиная с cardNum в заданном направлении
     * */
    private suspend fun playAnswer(
        cardNum: Int,
        toForward: Boolean
    ): Boolean {
        val cardInfo = resolveCardPosition(cardIndex = cardNum - 1, toForward = toForward) ?: return false
        playerStateRepository.updateRecordStateIfActive(
            PlayerRecordState(
                cardNum = cardInfo.cardIndex + 1,
                isQuestion = false,
                textContent = cardInfo.card.answer
            )
        )
        return true
    }

    /**
     * @return false если нечего проигрывать
     * */
    private suspend fun playNext(
        currentState: SpeechPlayerState.Active?
    ): Boolean {
        currentState ?: return false
        val curRecord = currentState.curRecord
        if (
            (curRecord.cardNum == currentState.cardIds.size) &&
            (currentState.onlyQuestions || curRecord.isQuestion.not())
        ) {
            return false
        }

        return if (currentState.onlyQuestions || curRecord.isQuestion.not()) {
            // следующий вопрос
            playQuestion(
                cardNum = curRecord.cardNum + 1,
                toForward = true
            )
        } else {
            // ответ
            playAnswer(
                cardNum = curRecord.cardNum,
                toForward = true
            )
        }
    }

    /**
     * @return false если нечего проигрывать
     * */
    private suspend fun playPrevious(
        currentState: SpeechPlayerState.Active?
    ): Boolean {
        currentState ?: return false
        val curRecord = currentState.curRecord
        if (
            (curRecord.cardNum <= 1) && curRecord.isQuestion
        ) {
            return false
        }

        return if (curRecord.isQuestion.not()) {
            // вопрос текущей карточки
            playQuestion(
                cardNum = curRecord.cardNum,
                toForward = false
            )
        } else if (currentState.onlyQuestions) {
            // вопрос пред карточки
            playQuestion(
                cardNum = curRecord.cardNum - 1,
                toForward = false
            )
        } else {
            // ответ пред карточки
            playAnswer(
                cardNum = curRecord.cardNum - 1,
                toForward = false
            )
        }
    }

    private fun pausePlayback() {
        playerStateRepository.updatePlayingIfActive(isPaused = true)
    }

    private fun resumePlayback() {
        playerStateRepository.updatePlayingIfActive(isPaused = false)
    }

    private fun nextCard() {
        scopeProvider.mainScope.launch {
            playNext(currentActiveState)
        }
    }

    private fun previousCard() {
        scopeProvider.mainScope.launch {
            playPrevious(currentActiveState)
        }
    }

    /** Остановить процесс проигрывания */
    private fun stopPlayback() {
        playerJob?.let {
            playerJob = null
            it.cancel()
        }
        playerStateRepository.disable()
        cardsCache.clear()
        currentActiveState = null
    }

    private suspend fun resolveCard(id: Long): Card? {
        if (!cardsCache.containsKey(id)) {
            cardsCache[id] = cardsRepository.getCard(id)
        }
        return cardsCache[id]
    }

    /**
     * Получить данные существующей карточки начиная с [cardIndex] вперед либо назад.
     * @return null если карточек не существует
     * */
    private suspend fun resolveCardPosition(cardIndex: Int, toForward: Boolean): CardPosition? {
        return if (toForward) {
            resolveCardPositionForward(cardIndex)
        } else {
            resolveCardPositionBackward(cardIndex)
        }
    }

    /**
     * Получить данные существующей карточки начиная с [cardIndex] либо далее по массиву.
     * @return null если карточек не существует
     * */
    private suspend fun resolveCardPositionForward(cardIndex: Int): CardPosition? {
        val cardIds = currentActiveState?.cardIds ?: return null

        var idx = cardIndex
        while (idx < cardIds.size) {
            resolveCard(cardIds[idx])?.let { card ->
                return CardPosition(card = card, cardIndex = idx)
            }
            idx++
        }
        return null
    }

    /**
     * Получить данные существующей карточки начиная с [cardIndex] либо ранее по массиву.
     * @return null если карточек не существует
     * */
    private suspend fun resolveCardPositionBackward(cardIndex: Int): CardPosition? {
        val cardIds = currentActiveState?.cardIds ?: return null

        var idx = cardIndex
        while (idx > -1) {
            resolveCard(cardIds[idx])?.let { card ->
                return CardPosition(card = card, cardIndex = idx)
            }
            idx--
        }
        return null
    }

    private data class CardPosition(
        val card: Card,
        val cardIndex: Int
    )
}