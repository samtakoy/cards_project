package ru.samtakoy.presentation.cards.screens.viewresult.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.utils.log.MyLog
import ru.samtakoy.domain.learncourse.CoursesPlanner
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.serialize.ParcelableSchedule
import ru.samtakoy.domain.learncourse.schedule.serialize.toDomain
import ru.samtakoy.domain.learncourse.schedule.serialize.toDomainOrEmpty
import ru.samtakoy.domain.learncourse.schedule.serialize.toParcelable
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.Action
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.Event
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModel.State
import ru.samtakoy.presentation.cards.screens.viewresult.vm.mapper.CardsViewResultMapper
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.common_err_message
import ru.samtakoy.resources.db_request_err_message

internal class CardsViewResultViewModelImpl(
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val coursesPlanner: CoursesPlanner,
    private val mapper: CardsViewResultMapper,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    private val viewItemId: Long,
    private val cardViewMode: CardViewMode
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoading = false,
        content = null
    )
), CardsViewResultViewModel {

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
        launchWithLoader {
            val viewItem = viewHistoryInteractor.getViewItem(viewItemId)!!
            if (
                viewItem.qPackId != 0L &&
                cardViewMode != CardViewMode.LEARNING &&
                viewItem.errorCardIds.isNotEmpty() &&
                !currentSchedule.value.isEmpty
            ) {
                // запланировать добавочные показы ошибочных карт
                coursesPlanner.planAdditionalCards(
                    qPackId = viewItem.qPackId,
                    errorCardIds = viewItem.errorCardIds,
                    schedule = currentSchedule.value
                )
            }
            sendAction(CardsViewResultViewModel.NavigationAction.CloseScreen)
        }
    }

    private fun subscribeData(viewItemId: Long, viewHistoryInteractor: ViewHistoryInteractor) {
        combine(
            viewHistoryInteractor.getViewHistoryItemAsFlow(viewItemId),
            currentSchedule.asFlow()
        ) { viewItem, schedule ->
            viewState = viewState.copy(
                content = mapper.map(cardViewMode, viewItem, schedule)
            )
        }
            .catch { onGetError(it) }
            .launchIn(mainScope)
    }

    private fun onGetError(t: Throwable) {
        launchCatching {
            MyLog.add(t.message ?: getString(Res.string.common_err_message), t)
            sendAction(
                Action.ShowErrorMessage(
                    getString(Res.string.db_request_err_message)
                )
            )
        }
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

    companion object {
        private const val KEY_SCHEDULE = "KEY_SCHEDULE"
    }
}