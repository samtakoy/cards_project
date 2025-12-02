package ru.samtakoy.speech.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.Dispatcher
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.data.card.CardsRepository
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.speech.domain.model.SpeechControlsListener
import ru.samtakoy.speech.domain.model.SpeechPlaybackState
import ru.samtakoy.speech.domain.model.TextToSpeechPlayer
import ru.samtakoy.speech.domain.model.TextToSpeechPlayerResult
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.player_answer_audio_title
import ru.samtakoy.resources.player_answer_text_title
import ru.samtakoy.resources.player_audio_ending_title
import ru.samtakoy.resources.player_question_audio_last_title
import ru.samtakoy.resources.player_question_audio_title
import ru.samtakoy.resources.player_question_audio_title_with_total
import ru.samtakoy.resources.player_question_text_title

internal class PlayCardsAudioUseCaseImpl(
    private val dispatcher: Dispatcher<SpeechControlsListener>,
    private val cardsRepository: CardsRepository,
    private val textToSpeechRepository: TextToSpeechRepository,
    private val scopeProvider: ScopeProvider
) : PlayCardsAudioUseCase {

    // TODO это будет в репозитории
    private val playbackState = MutableStateFlow<SpeechPlaybackState?>(null)
    private var playerJob: Job? = null
    private var onlyQuestions: Boolean = false
    private var cards: List<Card> = emptyList()
    private val controlsListener = object : SpeechControlsListener {
        override fun pausePlayback() = this@PlayCardsAudioUseCaseImpl.pausePlayback()
        override fun resumePlayback() = this@PlayCardsAudioUseCaseImpl.resumePlayback()
        override fun nextCard() = this@PlayCardsAudioUseCaseImpl.nextCard()
        override fun previousCard() = this@PlayCardsAudioUseCaseImpl.previousCard()
        override fun stopPlayback() = this@PlayCardsAudioUseCaseImpl.stopPlayback()
    }

    // TODO обработка ошибок
    override suspend fun playCards(
        cardIds: List<Long>,
        onlyQuestions: Boolean
    ) {
        this.onlyQuestions = onlyQuestions
        // TODO обработка результата
        val player = textToSpeechRepository.getPlayer() ?: return
        dispatcher.addListener(controlsListener)
        try {
            cards = cardsRepository.getCards(cardIds)

            // Стартовое состояние
            val currentCard = cards.getOrNull(0) ?: return
            playQuestion(currentCard, cardNum = 1, cardsTotal = cards.size)

            // Слушаем состояние и при изменении проигрываем звук либо нет
            @OptIn(ExperimentalCoroutinesApi::class)
            playerJob = playbackState
                .filterNotNull()
                .distinctUntilChanged()
                .mapLatest { state ->
                    updatePlayerByState(
                        state = state,
                        player = player,
                        onFinish = { playerJob?.cancel() }
                    )
                    state
                }
                .launchIn(scopeProvider.ioScope)
            playerJob?.join()
        } finally {
            dispatcher.removeListener(controlsListener)
            player.close()
            cards = emptyList()
        }
    }

    override fun observePlaybackState(): StateFlow<SpeechPlaybackState?> {
        return playbackState
    }

    private suspend fun updatePlayerByState(
        state: SpeechPlaybackState,
        player: TextToSpeechPlayer,
        onFinish: () -> Unit
    ) {
        if (state.isPaused) {
            player.stop()
        } else {
            // произносим
            val result = player.say(
                *(state.audio.toTypedArray())
            )
            when (result) {
                TextToSpeechPlayerResult.Canceled -> Unit // ничего не делаем - внешнее воздействие
                TextToSpeechPlayerResult.NotInited, // TODO
                is TextToSpeechPlayerResult.Failed,
                TextToSpeechPlayerResult.Succeed -> {
                    // если не отменено пользователем, то пробуем дальше
                    if (playNext(state).not()) {
                        // последняя карта
                        onFinish()
                    }
                }
            }
        }
    }

    private suspend fun playQuestion(
        currentCard: Card,
        cardNum: Int,
        cardsTotal: Int
    ) {
        playbackState.value = SpeechPlaybackState(
            currentCardNum = cardNum,
            totalCards = cardsTotal,
            isPaused = false,
            title = getString(Res.string.player_question_text_title, cardNum, cardsTotal),
            description = currentCard.question,
            audio = resolveQuestionAudio(cardNum, cardsTotal, currentCard),
            isQuestion = true
        )
    }

    private suspend fun playAnswer(
        currentCard: Card,
        cardNum: Int,
        cardsTotal: Int
    ) {
        playbackState.value = SpeechPlaybackState(
            currentCardNum = cardNum,
            totalCards = cardsTotal,
            isPaused = false,
            title = getString(Res.string.player_answer_text_title, cardNum, cardsTotal),
            description = currentCard.answer,
            audio = resolveAnswerAudio(cardNum, currentCard, cardsTotal),
            isQuestion = false
        )
    }

    private suspend fun resolveQuestionAudio(
        cardNum: Int,
        cardsTotal: Int,
        currentCard: Card
    ): List<String> = buildList<String> {
        if (cardNum == cardsTotal) {
            add(getString(Res.string.player_question_audio_last_title, cardNum))
        } else if (cardNum % 10 == 0) {
            add(getString(Res.string.player_question_audio_title_with_total, cardNum, cardsTotal))
        } else {
            add(getString(Res.string.player_question_audio_title, cardNum))
        }
        add(currentCard.question)
        if (cardNum == cardsTotal && onlyQuestions) {
            add(getString(Res.string.player_audio_ending_title))
        }
    }

    private suspend fun resolveAnswerAudio(
        cardNum: Int,
        currentCard: Card,
        cardsTotal: Int
    ): List<String> = buildList<String> {
        add(getString(Res.string.player_answer_audio_title, cardNum))
        add(currentCard.answer)
        if (cardNum == cardsTotal) {
            add(getString(Res.string.player_audio_ending_title))
        }
    }

    private suspend fun playNext(
        currentState: SpeechPlaybackState
    ): Boolean {
        if (
            (currentState.currentCardNum == currentState.totalCards) &&
            (onlyQuestions || currentState.isQuestion.not())
        ) {
            return false
        }

        if (onlyQuestions || currentState.isQuestion.not()) {
            // следующий вопрос
            playQuestion(
                currentCard = cards[currentState.currentCardNum],
                cardNum = currentState.currentCardNum + 1,
                cardsTotal = currentState.totalCards
            )
        } else {
            // ответ
            playAnswer(
                currentCard = cards[currentState.currentCardNum - 1],
                cardNum = currentState.currentCardNum,
                cardsTotal = currentState.totalCards
            )
        }
        return true
    }

    private suspend fun playPrevious(
        currentState: SpeechPlaybackState
    ): Boolean {
        if (
            (currentState.currentCardNum <= 1) && currentState.isQuestion
        ) {
            return false
        }

        if (currentState.isQuestion.not()) {
            // вопрос текущей карточки
            playQuestion(
                currentCard = cards[currentState.currentCardNum - 1],
                cardNum = currentState.currentCardNum,
                cardsTotal = currentState.totalCards
            )
        } else if (onlyQuestions) {
            // вопрос пред карточки
            playQuestion(
                currentCard = cards[currentState.currentCardNum - 2],
                cardNum = currentState.currentCardNum - 1,
                cardsTotal = currentState.totalCards
            )
        } else {
            // ответ пред карточки
            playAnswer(
                currentCard = cards[currentState.currentCardNum - 2],
                cardNum = currentState.currentCardNum - 1,
                cardsTotal = currentState.totalCards
            )
        }
        return true
    }

    private fun pausePlayback() {
        playbackState.value = playbackState.value?.copy(
            isPaused = true
        )
    }

    private fun resumePlayback() {
        playbackState.value = playbackState.value?.copy(
            isPaused = false
        )
    }

    private fun nextCard() {
        val currentState = playbackState.value ?: return
        scopeProvider.mainScope.launch {
            playNext(currentState)
        }
    }

    private fun previousCard() {
        val currentState = playbackState.value ?: return
        scopeProvider.mainScope.launch {
            playPrevious(currentState)
        }
    }

    private fun stopPlayback() {
        playerJob?.cancel()
    }
}