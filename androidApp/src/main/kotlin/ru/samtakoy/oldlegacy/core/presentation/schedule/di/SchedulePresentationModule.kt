package ru.samtakoy.oldlegacy.core.presentation.schedule.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.core.presentation.schedule.vm.ScheduleEditViewModelImpl

fun schedulePresentationModule() = module {
    viewModelOf(::ScheduleEditViewModelImpl)
}