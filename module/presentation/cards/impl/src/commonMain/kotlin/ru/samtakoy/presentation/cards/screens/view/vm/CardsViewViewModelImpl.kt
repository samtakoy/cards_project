package ru.samtakoy.presentation.cards.screens.view.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.learncourse.ViewHistoryProgressUseCase
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.presentation.cards.screens.view.model.BackupInfo
import ru.samtakoy.presentation.cards.screens.view.model.BackupInfoHolder
import ru.samtakoy.presentation.cards.screens.view.model.hasBackup
import ru.samtakoy.presentation.cards.screens.view.model.isAnswerEmpty
import ru.samtakoy.presentation.cards.screens.view.model.isQuestionEmpty
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.Action
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.Event
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.NavigationAction
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.State
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.AnswerButtonsMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.CardsViewViewStateMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.QuestionButtonsMapper
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.common_err_message
import ru.samtakoy.resources.db_request_err_message

internal class CardsViewViewModelImpl(
    private val cardInteractor: CardInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val viewHistoryProgressUseCase: ViewHistoryProgressUseCase,
    private val viewStateMapper: CardsViewViewStateMapper,
    questionButtonsMapper: QuestionButtonsMapper,
    answerButtonsMapper: AnswerButtonsMapper,
    private val savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    private val viewHistoryItemId: Long,
    private val viewMode: CardViewMode
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        type = State.Type.Initialization,
        isLoading = true,
        cardItems = listOf<CardsViewViewModel.CardState>().toImmutableList(),
        questionButtons = emptyList<MyButtonUiModel>().toImmutableList(),
        answerButtons = emptyList<MyButtonUiModel>().toImmutableList()
    )
), CardsViewViewModel {

    private val backupInfoHolder = SavedStateValue<BackupInfoHolder>(
        initialValueGetter = { BackupInfoHolder(emptyMap()) },
        keyName = KEY_BACKUP_INFO,
        savedStateHandle = savedStateHandle,
        serialize = { Json.encodeToString(it) },
        deserialize = { Json.decodeFromString(it) },
        saveScope = ioScope
    )

    /** Рабочее состояние текущей карточки */
    private val curCardState = SavedStateValue<CurCardState>(
        initialValueGetter = {
            CurCardState(type = CurCardState.Type.NOT_INITIALIZED, cardId = null, isStateSaved = false)
        },
        keyName = KEY_CUR_CARD_STATE,
        savedStateHandle = savedStateHandle,
        serialize = { Json.encodeToString(it) },
        deserialize = { Json.decodeFromString(it) },
        saveScope = ioScope
    )

    /** Данные из которых строится viewState */
    private val dataState = MutableStateFlow<DataState>(DataState(emptyList(), null))

    init {
        launchWithLoader {
            val viewHistoryItem = viewHistoryInteractor.getViewItem(viewHistoryItemId)!!
            val allCardsIds = viewHistoryItem.viewedCardIds + viewHistoryItem.todoCardIds
            viewState = viewState.copy(
                cardItems = createInitialCardItems(allCardsIds).toImmutableList(),
                questionButtons = questionButtonsMapper.map(viewMode).toImmutableList(),
                answerButtons = answerButtonsMapper.map(viewMode).toImmutableList()
            )
            dataState.update { DataState(allCardIds =  allCardsIds, cardInfo = null) }
            if (viewHistoryItem.todoCardIds.isEmpty()) {
                gotoResults()
            } else {
                curCardState.value = curCardState.value.copy(
                    type = CurCardState.Type.QUESTION,
                    cardId = viewHistoryItem.todoCardIds.firstOrNull(),
                    isStateSaved = true
                )
                subscribeData()
            }
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AnswerEditTextClick -> onUiAnswerEditTextClick()
            Event.QuestionEditTextClick -> onUiQuestionEditTextClick()
            is Event.AnswerTextChanged -> onUiAnswerTextChanged(event.newText)
            is Event.QuestionTextChanged -> onUiwQuestionTextChanged(event.newText)
            Event.RevertClick -> onUiRevertClick()
            is Event.ButtonClick -> onUiButtonClick(event.id)
            Event.FavoriteClick -> onUiFavoriteClick()
        }
    }

    private fun onUiFavoriteClick() {
        val cardState = getCurCardState() ?: return
        launchCatching(
            onError = ::onGetError
        ) {
            favoritesInteractor.setCardFavorite(
                cardId = cardState.id,
                favorite = if (cardState.content?.isFavorite == true) 0 else 1
            )
        }
    }

    private fun onUiButtonClick(buttonId: UiId) {
        when (buttonId) {
            QuestionButtonsMapper.IdPrevCardBtn -> onUiPrevCardClick()
            QuestionButtonsMapper.IdNextCardBtn -> onUiNextCardClick()
            QuestionButtonsMapper.IdViewAnswerBtn -> onUiViewAnswer()
            AnswerButtonsMapper.IdBackBtn -> onUiBackToQuestionClick()
            AnswerButtonsMapper.IdWrongBtn -> onUiWrongAnswer()
            AnswerButtonsMapper.IdNextCardBtn -> onUiNextCardClick()
        }
    }

    private fun onUiPrevCardClick() {
        launchWithLoader {
            val newCurrentCardId: Long? = viewHistoryProgressUseCase.rollbackToPrevCard(
                viewItemId = viewHistoryItemId
            )
            if (newCurrentCardId == null) {
                sendAction(NavigationAction.CloseScreen)
            } else {
                curCardState.value = curCardState.value.copy(
                    type = CurCardState.Type.QUESTION,
                    cardId = newCurrentCardId
                )
            }
        }
    }

    private fun onUiViewAnswer() {
        curCardState.value = curCardState.value.copy(type = CurCardState.Type.ANSWER)
    }

    private fun onUiNextCardClick() {
        switchToNextCard(withError = false)
    }

    private fun onUiWrongAnswer() {
        switchToNextCard(withError = true)
    }

    private fun onUiBackToQuestionClick() {
        curCardState.value = curCardState.value.copy(type = CurCardState.Type.QUESTION)
    }

    private fun onUiQuestionEditTextClick() {
        sendAction(
            Action.ShowEditTextDialog(
                text = dataState.value.cardInfo?.card?.question.orEmpty(),
                question = true
            )
        )
    }

    private fun onUiAnswerEditTextClick() {
        sendAction(
            Action.ShowEditTextDialog(
                text = dataState.value.cardInfo?.card?.answer.orEmpty(),
                question = false
            )
        )
    }

    private fun onUiwQuestionTextChanged(newText: String) {
        val card = dataState.value.cardInfo?.card ?: return
        val prevText = card.question
        launchWithLoader {
            cardInteractor.setCardNewQuestionText(card.id, newText)
            updateBackupInfo(cardId = card.id) { backupInfo ->
                if (backupInfo.isQuestionEmpty()) {
                    backupInfo.copy(question = prevText)
                } else {
                    backupInfo
                }
            }
        }
    }

    private fun onUiAnswerTextChanged(newText: String) {
        val card = dataState.value.cardInfo?.card ?: return
        val prevText = card.answer
        launchWithLoader {
            cardInteractor.setCardNewAnswerText(card.id, newText)
            updateBackupInfo(cardId = card.id) { backupInfo ->
                if (backupInfo.isAnswerEmpty()) {
                    backupInfo.copy(answer = prevText)
                } else {
                    backupInfo
                }
            }
        }
    }

    private fun onUiRevertClick() {
        val card = dataState.value.cardInfo?.card ?: return
        val backupInfo = backupInfoHolder.value.cardIdToBackupMap[card.id] ?: return
        val isOnAnswer: Boolean = curCardState.value.type == CurCardState.Type.ANSWER

        if (backupInfo.hasBackup(isOnAnswer)) {
            if (isOnAnswer) {
                revertAnswerToBackup(card.id, backupInfo)
            } else {
                revertQuestionToBackup(card.id, backupInfo)
            }
        }
    }

    private fun revertAnswerToBackup(cardId: Long, backupInfo: BackupInfo) {
        if (backupInfo.isAnswerEmpty()) return
        launchWithLoader {
            cardInteractor.setCardNewAnswerText(cardId, backupInfo.answer.orEmpty())
            updateBackupInfo(cardId = cardId) { backupInfo ->
                backupInfo.copy(answer = null)
            }
        }
    }

    private fun revertQuestionToBackup(cardId: Long, backupInfo: BackupInfo) {
        if (backupInfo.isQuestionEmpty()) return
        launchWithLoader {
            cardInteractor.setCardNewQuestionText(cardId, backupInfo.question.orEmpty())
            updateBackupInfo(cardId = cardId) { backupInfo ->
                backupInfo.copy(question = null)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun subscribeData() {
        getCurrentCardWithBackupAsFlow()
            .onEach { cardWithInfo ->
                dataState.update {
                    it.copy(cardInfo = cardWithInfo)
                }
            }
            .catch {
                onGetError(it)
            }
            .launchIn(mainScope)

        dataState
            .onEach {
                val prevCard = (viewState.type as? State.Type.Card)
                val testCard = it.cardInfo?.state
                viewState = viewState.copy(
                    type = viewStateMapper.mapStateType(
                        dataState = it,
                        viewMode = viewMode,
                        viewHistoryItemId = viewHistoryItemId
                    )
                )
            }
            .mapLatest { it.cardInfo }
            .distinctUntilChanged()
            .onEach { cardInfo -> updateCardInList(cardInfo)}
            .catch {
                onGetError(it)
            }
            .launchIn(mainScope)
    }

    private fun getCurrentCardWithBackupAsFlow(): Flow<CardInfo> {
        return combine(
            getCurrentCardAsFlow(),
            backupInfoHolder.asFlow(),
        ) { cardStateAndData, backupInfoHolder ->
            val card: Card? = cardStateAndData.second
            CardInfo(
                state = cardStateAndData.first,
                card = card,
                backup = card?.let { backupInfoHolder.cardIdToBackupMap[card.id] } ?: BackupInfo()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCurrentCardAsFlow(): Flow<Pair<CurCardState, Card?>> {
        return curCardState
            .asFlow()
            .distinctUntilChanged()
            .flatMapLatest { cardState ->
                if (cardState.cardId != null) {
                    cardInteractor.getCardAsFlow(cardState.cardId).map { card ->
                        Pair(cardState, card)
                    }
                } else {
                    flowOf(Pair(cardState, null))
                }
            }
    }

    private fun switchToNextCard(withError: Boolean) {
        val curCardId: Long? = curCardState.value.cardId
        if (curCardId == null) {
            launchCatching {
                sendAction(
                    Action.ShowErrorMessage(
                        getString(Res.string.common_err_message)
                    )
                )
            }
            return
        }

        launchWithLoader {
            val resultViewItem = viewHistoryProgressUseCase.makeViewedCurCard(
                viewItemId = viewHistoryItemId,
                curCardId = curCardId,
                withError = withError
            )!!
            if (resultViewItem.todoCardIds.isEmpty()) {
                gotoResults()
            } else {
                curCardState.value = curCardState.value.copy(
                    type = CurCardState.Type.QUESTION,
                    cardId = resultViewItem.todoCardIds.first()
                )
            }
        }
    }

    private fun gotoResults() {
        sendAction(NavigationAction.OpenResults(viewHistoryItemId = viewHistoryItemId, cardViewMode = viewMode))
    }

    private fun getCurCardState(): CardsViewViewModel.CardState? {
        val cardIndex = (viewState.type as? State.Type.Card)?.cardIndex ?: return null
        return viewState.cardItems[cardIndex]
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(t.message.orEmpty(), t)
        launchCatching {
            sendAction(
                Action.ShowErrorMessage(
                    getString(Res.string.db_request_err_message)
                )
            )
        }
    }

    private fun updateBackupInfo(
        cardId: Long,
        updateBlock: (BackupInfo) -> BackupInfo
    ) {
        val backupInfo = backupInfoHolder.value.cardIdToBackupMap[cardId] ?: BackupInfo()
        backupInfoHolder.value = backupInfoHolder.value.copy(
            cardIdToBackupMap = backupInfoHolder.value.cardIdToBackupMap.toMutableMap().also {
                it[cardId] = updateBlock(backupInfo)
            }
        )
    }

    private fun createInitialCardItems(cardIds: List<Long>): List<CardsViewViewModel.CardState> {
        return buildList {
            cardIds.forEach {
                add(
                    CardsViewViewModel.CardState(
                        id = it,
                        isQuestion = true,
                        content = null
                    )
                )
                add(
                    CardsViewViewModel.CardState(
                        id = it,
                        isQuestion = false,
                        content = null
                    )
                )
            }
        }
    }

    private fun updateCardInList(info: CardInfo?) {
        val card: Card = info?.card ?: return
        viewState = viewState.copy(
            cardItems = viewState.cardItems.map { cardState ->
                if (card.id == cardState.id) {
                    cardState.copy(
                        content = if (cardState.isQuestion) {
                            CardsViewViewModel.CardState.Content(
                                isFavorite = card.favorite > 0,
                                text = card.question.asAnnotated(),
                                hasRevertButton = info.backup.hasBackup(cardState.isQuestion)
                            )
                        } else {
                            CardsViewViewModel.CardState.Content(
                                isFavorite = card.favorite > 0,
                                text = card.answer.asAnnotated(),
                                hasRevertButton = info.backup.hasBackup(cardState.isQuestion)
                            )
                        }
                    )
                } else {
                    cardState
                }
            }.toImmutableList()
        )
    }

    private fun launchWithLoader(
        onError: (suspend (Throwable) -> Unit)? = ::onGetError,
        block: suspend () -> Unit
    ) {
        viewState = viewState.copy(isLoading = true)
        launchCatching(
            onError = onError,
            onFinally = { viewState = viewState.copy(isLoading = false) }
        ) {
            block()
        }
    }

    /**
     * Текущие данные экрана.
     */
    internal data class DataState(
        val allCardIds: List<Long>,
        val cardInfo: CardInfo?
    )

    /** Сохраняемое состояние, с которым работаем */
    @Serializable
    internal data class CurCardState(
        /** идентификатор состояния  */
        val type: Type,
        /** какую карточку смотрим? */
        val cardId: Long?,
        /** Состояние сохранено */
        val isStateSaved: Boolean
    ) {
        enum class Type {
            NOT_INITIALIZED,
            QUESTION,
            ANSWER
        }
    }

    /**
     * Данные текущей карточки
     * @param state состояние, выставляемое логикой прогаммы
     * @param card данные текущей карточки из БД
     * @param backup информация для отката текста текущей карточки в случае, если было редактирование.
     * */
    internal data class CardInfo(
        val state: CurCardState,
        val card: Card?,
        val backup: BackupInfo
    )

    companion object {
        private const val KEY_CUR_CARD_STATE = "KEY_CUR_CARD_STATE"
        private const val KEY_BACKUP_INFO = "KEY_BACKUP_INFO"
    }
}