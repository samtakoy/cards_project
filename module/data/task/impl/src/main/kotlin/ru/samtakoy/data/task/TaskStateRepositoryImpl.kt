package ru.samtakoy.data.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.task.model.TaskStateId

internal class TaskStateRepositoryImpl : TaskStateRepository {

    private val states = mutableMapOf<TaskStateId, MutableStateFlow<TaskStateData>>()
    private val lock = Mutex()

    override suspend fun getTaskState(id: TaskStateId): TaskStateData = lock.withLock {
        return getOrCreateTaskStateAsFlow(id).value
    }

    override suspend fun getTaskStateAsFlow(id: TaskStateId): Flow<TaskStateData> = lock.withLock {
        return getOrCreateTaskStateAsFlow(id)
    }

    override suspend fun updateTaskState(
        id: TaskStateId,
        state: TaskStateData
    ) = lock.withLock {
        getOrCreateTaskStateAsFlow(id).value = state
    }

    private fun getOrCreateTaskStateAsFlow(id: TaskStateId): MutableStateFlow<TaskStateData> {
        return states.getOrPut(id) {
            MutableStateFlow(TaskStateData.Unknown)
        }
    }
}