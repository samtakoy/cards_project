package ru.samtakoy.data.task.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.samtakoy.data.task.TaskStateRepository
import ru.samtakoy.data.task.TaskStateRepositoryImpl

fun taskStateDataModule() = module {
    singleOf<TaskStateRepository>(::TaskStateRepositoryImpl)
}