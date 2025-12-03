package ru.samtakoy.oldlegacy.core.presentation.schedule.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import ru.samtakoy.R
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.base.viewmodel.savedstate.SavedStateValue
import ru.samtakoy.domain.learncourse.schedule.Schedule
import ru.samtakoy.domain.learncourse.schedule.ScheduleItem
import ru.samtakoy.domain.learncourse.schedule.ScheduleTimeUnit
import ru.samtakoy.domain.learncourse.schedule.serialize.ParcelableSchedule
import ru.samtakoy.domain.learncourse.schedule.serialize.toDomain
import ru.samtakoy.domain.learncourse.schedule.serialize.toParcelable
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.utils.toStringView
import java.util.TreeSet

internal class ScheduleEditViewModelImpl(
    private val resources: Resources,
    private val savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider,
    initialSchedule: Schedule
) : BaseViewModelImpl<ScheduleEditViewModel.State, ScheduleEditViewModel.Action, ScheduleEditViewModel.Event>(
    scopeProvider = scopeProvider,
    initialState = ScheduleEditViewModel.State(
        schedule = "",
        scheduleCurItemButtonText = ""
    )
), ScheduleEditViewModel {

    private val schedule = SavedStateValue<Schedule>(
        initialValueGetter = { initialSchedule },
        keyName = KEY_SCHEDULE,
        savedStateHandle = savedStateHandle,
        serialize = { Json.encodeToString(it.toParcelable()) },
        deserialize = { (Json.decodeFromString(it) as ParcelableSchedule).toDomain() },
        saveScope = ioScope
    )

    private val curItemToAdd = SavedStateValue<ScheduleItem>(
        initialValueGetter = { ScheduleItem(1, ScheduleTimeUnit.MINUTE) },
        keyName = KEY_SCHEDULE_ITEM,
        savedStateHandle = savedStateHandle,
        serialize = { Json.encodeToString(it.toParcelable()) },
        deserialize = { (Json.decodeFromString(it) as ParcelableSchedule.Item).toDomain() },
        saveScope = ioScope
    )

    init {
        subscribeData()
    }

    override fun onEvent(event: ScheduleEditViewModel.Event) {
        when (event) {
            ScheduleEditViewModel.Event.ClearScheduleClick -> {
                schedule.value = Schedule(emptyList())
            }
            ScheduleEditViewModel.Event.ClearScheduleCurItemClick -> {
                curItemToAdd.value = ScheduleItem(1, ScheduleTimeUnit.MINUTE)
            }
            ScheduleEditViewModel.Event.AddScheduleCurItem -> {
                schedule.value = Schedule(
                    items = (TreeSet(schedule.value.items) + curItemToAdd.value).toList()
                )
            }
            is ScheduleEditViewModel.Event.ScheduleTimeUnitSelect -> {
                if (event.unit == curItemToAdd.value.timeUnit) {
                    curItemToAdd.value = ScheduleItem(1 + curItemToAdd.value.dimension, event.unit)
                } else {
                    curItemToAdd.value = ScheduleItem(1, event.unit)
                }
            }
            ScheduleEditViewModel.Event.ConfirmResultClick -> {
                sendAction(
                    ScheduleEditViewModel.Action.CloseWithResult(serializedSchedule = schedule.value.toParcelable())
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
                        it.toStringView()
                    }
                )
            }
            .launchIn(mainScope)
        curItemToAdd.asFlow()
            .onEach {
                viewState = viewState.copy(
                    scheduleCurItemButtonText = it.toStringView()
                )
            }
            .launchIn(mainScope)
    }

    companion object {
        private const val KEY_SCHEDULE = "KEY_SCHEDULE"
        private const val KEY_SCHEDULE_ITEM = "KEY_SCHEDULE_ITEM"
    }
}