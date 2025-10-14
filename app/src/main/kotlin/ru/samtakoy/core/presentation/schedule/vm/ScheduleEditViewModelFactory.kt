package ru.samtakoy.core.presentation.schedule.vm

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.learncourse.schedule.Schedule

internal class ScheduleEditViewModelFactory @AssistedInject constructor(
    private val resources: Resources,
    private val scopeProvider: ScopeProvider,
    @Assisted
    private val schedule: Schedule
): AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        require(modelClass == ScheduleEditViewModelImpl::class.java)
        return ScheduleEditViewModelImpl(
            resources = resources,
            savedStateHandle = handle,
            scopeProvider = scopeProvider,
            initialSchedule = schedule
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(schedule: Schedule): ScheduleEditViewModelFactory
    }
}