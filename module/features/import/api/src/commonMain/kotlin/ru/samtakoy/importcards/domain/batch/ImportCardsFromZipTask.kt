package ru.samtakoy.importcards.domain.batch

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.task.model.TaskStateId

interface ImportCardsFromZipTask {
    val taskId: TaskStateId
    suspend fun import(zipFile: PlatformFile, opts: ImportCardsOpts): TaskStateData
    suspend fun getStatusAsFlow(): Flow<TaskStateData>
    suspend fun reset()
}