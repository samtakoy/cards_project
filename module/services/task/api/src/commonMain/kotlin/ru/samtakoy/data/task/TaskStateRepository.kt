package ru.samtakoy.data.task

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.task.model.TaskStateId

interface TaskStateRepository { // TODO domain
    suspend fun getTaskState(id: TaskStateId): TaskStateData
    suspend fun getTaskStateAsFlow(id: TaskStateId): Flow<TaskStateData>
    suspend fun updateTaskState(id: TaskStateId, state: TaskStateData)
}