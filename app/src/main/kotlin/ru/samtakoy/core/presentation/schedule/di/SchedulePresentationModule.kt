package ru.samtakoy.core.presentation.schedule.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModelImpl

fun schedulePresentationModule() = module {
    viewModelOf(::ScheduleEditViewModelImpl)
}