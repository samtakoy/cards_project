package ru.samtakoy.core.presentation.schedule.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.Action
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.Event
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.State
import ru.samtakoy.features.learncourse.domain.model.schedule.Schedule
import ru.samtakoy.features.learncourse.domain.model.schedule.ScheduleItem
import ru.samtakoy.features.learncourse.domain.model.schedule.ScheduleTimeUnit
import ru.samtakoy.features.learncourse.domain.model.schedule.serialize.ParcelableSchedule
import ru.samtakoy.features.learncourse.domain.model.schedule.serialize.toDomain
import ru.samtakoy.features.learncourse.domain.model.schedule.serialize.toParcelable
import java.util.TreeSet

internal class ScheduleEditViewModelImpl(
    private val resources: Resources,
    private val savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    initialSchedule: Schedule
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        schedule = "",
        scheduleCurItemButtonText = ""
    )
), ScheduleEditViewModel {

    private val schedule = SavedStateValue<Schedule>(
        initialValueGetter = { initialSchedule },
        keyName = KEY_SCHEDULE,
        savedStateHandle = savedStateHandle,
        serialize = { it.toParcelable() },
        deserialize = { (it as ParcelableSchedule).toDomain() },
        saveScope = ioScope
    )

    private val curItemToAdd = SavedStateValue<ScheduleItem>(
        initialValueGetter = { ScheduleItem(1, ScheduleTimeUnit.MINUTE) },
        keyName = KEY_SCHEDULE_ITEM,
        savedStateHandle = savedStateHandle,
        serialize = { it.toParcelable() },
        deserialize = { (it as ParcelableSchedule.Item).toDomain() },
        saveScope = ioScope
    )

    init {
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.ClearScheduleClick -> {
                schedule.value = Schedule(emptyList())
            }
            Event.ClearScheduleCurItemClick -> {
                curItemToAdd.value = ScheduleItem(1, ScheduleTimeUnit.MINUTE)
            }
            Event.AddScheduleCurItem -> {
                schedule.value = Schedule(
                    items = (TreeSet(schedule.value.items) + curItemToAdd.value).toList()
                )
            }
            is Event.ScheduleTimeUnitSelect -> {
                if (event.unit == curItemToAdd.value.timeUnit) {
                    curItemToAdd.value = ScheduleItem(1 + curItemToAdd.value.dimension, event.unit)
                } else {
                    curItemToAdd.value = ScheduleItem(1, event.unit)
                }
            }
            Event.ConfirmResultClick -> {
                sendAction(
                    Action.CloseWithResult(serializedSchedule = schedule.value.toParcelable())
                )
            }
        }
    }

    private fun subscribeData() {
        schedule.asFlow()
            .onEach {
                viewState = viewState.copy(
                    schedule = if (it.isEmpty) {
                        resources.getString(R.string.schedule_none)
                    } else {
                        it.toStringView(resources)
                    }
                )
            }
            .launchIn(mainScope)
        curItemToAdd.asFlow()
            .onEach {
                viewState = viewState.copy(
                    scheduleCurItemButtonText = it.toStringView(resources)
                )
            }
            .launchIn(mainScope)
    }

    companion object {
        private const val KEY_SCHEDULE = "KEY_SCHEDULE"
        private const val KEY_SCHEDULE_ITEM = "KEY_SCHEDULE_ITEM"
    }
}