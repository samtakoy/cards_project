package ru.samtakoy.importcards.platform

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.coroutines.ScopeProvider
import ru.samtakoy.data.task.TaskStateRepository
import ru.samtakoy.importcards.domain.batch.RunImportCardsFromZipTaskUseCase
import ru.samtakoy.importcards.domain.batch.ImportCardsZipUseCase
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.task.model.TaskStateId
import ru.samtakoy.domain.task.model.isFinished
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.batch_zip_import_status_init
import ru.samtakoy.resources.common_err_message

internal class RunImportCardsFromZipTaskUseCaseImpl(
    private val scopeProvider: ScopeProvider,
    private val taskStateRepository: TaskStateRepository,
    private val importCardsUseCase: ImportCardsZipUseCase
) : RunImportCardsFromZipTaskUseCase {

    private var taskJob: Job? = null

    override val taskId: TaskStateId
        get() = TaskStateId.ImportFromZip

    override suspend fun import(
        zipFile: PlatformFile,
        opts: ImportCardsOpts
    ): TaskStateData {
        reset()
        taskStateRepository.updateTaskState(id = taskId, state = TaskStateData.Init)

        taskJob = scopeProvider.ioScope.launch {
            try {
                reportProgress(
                    TaskStateData.ActiveStatus(
                        getString(Res.string.batch_zip_import_status_init)
                    )
                )
                importCardsUseCase.import(
                    zipFile = zipFile,
                    opts = opts
                ) { taskState ->
                    reportProgress(taskState)
                }
            } catch (e: Exception) {
                reportProgress(
                    TaskStateData.Error(
                        e.message ?: getString(Res.string.common_err_message)
                    )
                )
            }
        }
        taskJob?.join()

        val taskState = taskStateRepository.getTaskState(taskId)
        return if (!taskState.isFinished()) {
            val newTaskState = TaskStateData.Error("chto-to poshlo ne tak")
            taskStateRepository.updateTaskState(taskId, newTaskState)
            newTaskState
        } else {
            taskState
        }
    }

    override suspend fun getStatusAsFlow(): Flow<TaskStateData> {
        return taskStateRepository.getTaskStateAsFlow(taskId)
    }

    override suspend fun reset() {
        taskJob?.let {
            taskJob = null
            it.cancel()
        }
    }

    private suspend fun reportProgress(
        taskState: TaskStateData
    ) {
        taskStateRepository.updateTaskState(
            id = taskId,
            state = taskState
        )
    }
}