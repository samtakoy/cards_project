package ru.samtakoy.core.presentation.cards.result.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Action
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Event
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.State
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.serialize.ParcelableSchedule
import ru.samtakoy.domain.learncourse.schedule.serialize.toDomain
import ru.samtakoy.domain.learncourse.schedule.serialize.toDomainOrEmpty
import ru.samtakoy.domain.learncourse.schedule.serialize.toParcelable
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.presentation.utils.toStringView

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
        initialValueGetter = { Schedule(emptyList()) },
        keyName = KEY_SCHEDULE,
        savedStateHandle = savedStateHandle,
        serialize = { it.toParcelable() },
        deserialize = { (it as ParcelableSchedule).toDomain() },
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

    private fun onNewScheduleSet(serializedSchedule: ParcelableSchedule?) {
        currentSchedule.value = serializedSchedule.toDomainOrEmpty()
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