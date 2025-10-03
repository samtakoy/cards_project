package ru.samtakoy.core.presentation.cards.result.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Action
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Event
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.State
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.features.views.domain.ViewHistoryInteractor

class CardsViewResultViewModelImpl(
    viewHistoryInteractor: ViewHistoryInteractor,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    viewItemId: Long,
    cardViewMode: CardViewMode
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLearnView = cardViewMode === CardViewMode.LEARNING,
        viewedCardsCount = 0,
        errorCardsCount = 0,
        newSchedule = resources.getString(R.string.schedule_none)
    )
) {
    private val currentSchedule = SavedStateValue<Schedule>(
        initialValueGetter = { Schedule() },
        keyName = KEY_SCHEDULE,
        savedStateHandle = savedStateHandle,
        serialize = { it.serializeToString() },
        deserialize = { Schedule.deserializeFrom(it as String) },
        saveScope = ioScope
    )

    init {
        subscribeData(
            viewItemId = viewItemId,
            viewHistoryInteractor = viewHistoryInteractor
        )
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.NewScheduleDialogResult -> onNewScheduleSet(event.serializedSchedule)
            Event.OkBtnClick -> onUiOkClick()
            Event.ScheduleBtnClick -> onUiScheduleClick()
        }
    }

    private fun onUiScheduleClick() {
        sendAction(Action.ShowScheduleEditDialog(currentSchedule.value))
    }

    private fun onNewScheduleSet(serializedSchedule: String?) {
        currentSchedule.value = Schedule.deserializeFrom(serializedSchedule)
    }

    private fun onUiOkClick() {
        sendAction(Action.ResultOk(currentSchedule.value))
    };

    private fun subscribeData(viewItemId: Long, viewHistoryInteractor: ViewHistoryInteractor) {
        viewHistoryInteractor
            .getViewHistoryItemAsFlow(viewItemId)
            .catch { onGetError(it) }
            .onEach { viewItem ->
                viewState = viewState.copy(
                    viewedCardsCount = viewItem?.viewedCardIds?.size ?: 0,
                    errorCardsCount = viewItem?.errorCardIds?.size ?: 0
                )
            }
            .launchIn(mainScope)
        currentSchedule
            .asFlow()
            .onEach {
                viewState = viewState.copy(
                    newSchedule = if (!it.isEmpty) {
                        it.toStringView(resources)
                    } else {
                        resources.getString(R.string.schedule_none)
                    }
                )
            }
            .launchIn(mainScope)
    }

    private fun onGetError(t: Throwable?) {
        MyLog.add(ExceptionUtils.getMessage(t), t)
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message))
        )
    }

    companion object {
        private const val KEY_SCHEDULE = "KEY_SCHEDULE"
    }
}