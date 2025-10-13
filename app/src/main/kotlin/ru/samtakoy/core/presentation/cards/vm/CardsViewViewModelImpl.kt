package ru.samtakoy.core.presentation.cards.vm

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.core.presentation.cards.types.BackupInfo
import ru.samtakoy.core.presentation.cards.types.BackupInfoHolder
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.cards.types.hasBackup
import ru.samtakoy.core.presentation.cards.types.isAnswerEmpty
import ru.samtakoy.core.presentation.cards.types.isQuestionEmpty
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModel.Action
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModel.Event
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModel.State
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.card.domain.model.Card
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryItem
import ru.samtakoy.domain.view.ViewHistoryProgressUseCase

internal class CardsViewViewModelImpl(
    private val cardInteractor: CardInteractor,
    private val mViewHistoryInteractor: ViewHistoryInteractor,
    private val mViewHistoryProgressUseCase: ViewHistoryProgressUseCase,
    private val mCoursesPlanner: CoursesPlanner,
    private val viewStateMapper: CardsViewViewStateMapper,
    private val resources: Resources,
    private val savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    private val viewHistoryItemId: Long,
    private val viewMode: CardViewMode
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        type = State.Type.Initialization,
        viewMode = viewMode,
        isLoading = true
    )
), CardsViewViewModel {

    private val backupInfoHolder = SavedStateValue<BackupInfoHolder>(
        initialValueGetter = { BackupInfoHolder(emptyMap()) },
        keyName = KEY_BACKUP_INFO,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as BackupInfoHolder },
        saveScope = ioScope
    )

    /** Рабочее состояние текущей карточки */
    private val curCardState = SavedStateValue<CurCardState>(
        initialValueGetter = {
            CurCardState(type = CurCardState.Type.NOT_INITIALIZED, cardId = null, isStateSaved = false)
        },
        keyName = KEY_CUR_CARD_STATE,
        savedStateHandle = savedStateHandle,
        serialize = { it },
        deserialize = { it as CurCardState },
        saveScope = ioScope
    )

    /** Данные из которых строится viewState */
    private val dataState = MutableStateFlow<DataState?>(null)

    init {
        launchWithLoader {
            if (curCardState.value.isStateSaved.not()) {
                // Начальное состояние
                val viewHistoryItem = mViewHistoryInteractor.getViewItem(viewHistoryItemId)!!
                curCardState.value = curCardState.value.copy(
                    type = if (viewHistoryItem.todoCardIds.isEmpty()) {
                        CurCardState.Type.RESULTS
                    } else {
                        CurCardState.Type.QUESTION
                    },
                    cardId = viewHistoryItem.todoCardIds.firstOrNull(),
                    isStateSaved = true
                )
            }
            subscribeData()
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AnswerEditTextClick -> onUiAnswerEditTextClick()
            Event.QuestionEditTextClick -> onUiQuestionEditTextClick()
            Event.BackToQuestionClick -> onUiBackToQuestionClick()
            Event.NextCardClick -> onUiNextCardClick()
            Event.PrevCardClick -> onUiPrevCardClick()
            is Event.AnswerTextChanged -> onUiAnswerTextChanged(event.newText)
            is Event.QuestionTextChanged -> onUiwQuestionTextChanged(event.newText)
            Event.RevertClick -> onUiRevertClick()
            is Event.ScheduleEditResultOk -> onUiNewScheduleResultOk(event.newErrorCardsSchedule)
            Event.ViewAnswerClick -> onUiViewAnswer()
            Event.WrongAnswerClick -> onUiWrongAnswer()
        }
    }

    private fun onUiPrevCardClick() {
        launchWithLoader {
            val newCurrentCardId: Long? = mViewHistoryProgressUseCase.rollbackToPrevCard(
                viewItemId = viewHistoryItemId
            )
            if (newCurrentCardId == null) {
                sendAction(CardsViewViewModel.NavigationAction.CloseScreen)
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

    private fun onUiNewScheduleResultOk(newErrorCardsSchedule: Schedule) {
        val viewItem = dataState.value?.viewHistoryItem ?: return
        if (
            viewItem.qPackId != 0L &&
            viewMode != CardViewMode.LEARNING &&
            viewItem.errorCardIds.isNotEmpty() &&
            !newErrorCardsSchedule.isEmpty
        ) {
            // запланировать добавочные показы ошибочных карт
            mCoursesPlanner.planAdditionalCards(
                viewItem.qPackId,
                viewItem.errorCardIds,
                newErrorCardsSchedule
            )
        }
        sendAction(CardsViewViewModel.NavigationAction.CloseScreen)
    }

    private fun onUiQuestionEditTextClick() {
        sendAction(
            Action.ShowEditTextDialog(
                text = dataState.value?.cardInfo?.card?.question.orEmpty(),
                question = true
            )
        )
    }

    private fun onUiAnswerEditTextClick() {
        sendAction(
            Action.ShowEditTextDialog(
                text = dataState.value?.cardInfo?.card?.answer.orEmpty(),
                question = false
            )
        )
    }

    private fun onUiwQuestionTextChanged(newText: String) {
        val card = dataState.value?.cardInfo?.card ?: return
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
        val card = dataState.value?.cardInfo?.card ?: return
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
        val card = dataState.value?.cardInfo?.card ?: return
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

    private fun subscribeData() {
        combine(
            mViewHistoryInteractor.getViewHistoryItemAsFlow(viewHistoryItemId),
            getCurrentCardWithBackupAsFlow()
        ) { viewHistoryItem, cardWithInfo ->
            dataState.update {
                if (viewHistoryItem != null) {
                    DataState(
                        viewHistoryItem = viewHistoryItem,
                        cardInfo = cardWithInfo
                    )
                } else {
                    null
                }
            }
        }
            .catch {
                onGetError(it)
            }
            .launchIn(mainScope)

        dataState
            .onEach {
                viewState = viewState.copy(
                    type = viewStateMapper.mapStateType(
                        dataState = it,
                        viewMode = viewMode
                    )
                )
            }
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
            sendAction(
                Action.ShowErrorMessage(resources.getString(R.string.common_err_message))
            )
            return
        }

        launchWithLoader {
            val resultViewItem = mViewHistoryProgressUseCase.makeViewedCurCard(
                viewItemId = viewHistoryItemId,
                curCardId = curCardId,
                withError = withError
            )!!
            if (resultViewItem.todoCardIds.isEmpty()) {
                curCardState.value = curCardState.value.copy(
                    type = CurCardState.Type.RESULTS,
                    cardId = null
                )
            } else {
                curCardState.value = curCardState.value.copy(
                    type = CurCardState.Type.QUESTION,
                    cardId = resultViewItem.todoCardIds.first()
                )
            }
        }
    }

    private fun onGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t), t)
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message))
        )
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
        val viewHistoryItem: ViewHistoryItem,
        val cardInfo: CardInfo?
    )

    /** Сохраняемое состояние, с которым работаем */
    @Parcelize
    internal data class CurCardState(
        /** идентификатор состояния  */
        val type: Type,
        /** какую карточку смотрим? */
        val cardId: Long?,
        /** Состояние сохранено */
        val isStateSaved: Boolean
    ) : Parcelable {
        enum class Type {
            NOT_INITIALIZED,
            QUESTION,
            ANSWER,
            RESULTS
        }
    }

    /**
     * Данные текущей карточки
     * @param state состояние, выставляемое логикой прогаммы
     * @param card данные теекущей карточки из БД
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