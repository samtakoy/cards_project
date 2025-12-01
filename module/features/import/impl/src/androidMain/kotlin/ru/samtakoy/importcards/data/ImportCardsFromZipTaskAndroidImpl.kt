package ru.samtakoy.importcards.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import ru.samtakoy.data.task.TaskStateRepository
import ru.samtakoy.importcards.domain.batch.ImportCardsFromZipTask
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.task.model.TaskStateId
import ru.samtakoy.domain.task.model.isFinished

internal class ImportCardsFromZipTaskAndroidImpl(
    private val context: Context,
    private val taskStateRepository: TaskStateRepository
) : ImportCardsFromZipTask {

    override val taskId: TaskStateId
        get() = TaskStateId.ImportFromZip

    override suspend fun import(zipFile: PlatformFile, opts: ImportCardsOpts): TaskStateData {
        reset()
        val workManager = WorkManager.getInstance(context)

        taskStateRepository.updateTaskState(id = taskId, state = TaskStateData.Init)

        val workRequest = OneTimeWorkRequestBuilder<ImportCardsFromZipWorker>()
            // .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(
                Data.Builder()
                    .putString(
                        ImportCardsFromZipWorker.PARAM_ZIP_FILE,
                        ImportCardsFromZipWorker.serializeFile(zipFile)
                    )
                    .putString(
                        ImportCardsFromZipWorker.PARAM_OPTS,
                        ImportCardsFromZipWorker.serializeOpts(opts)
                    )
                    .putString(
                        ImportCardsFromZipWorker.PARAM_NAME_TASK_ID,
                        ImportCardsFromZipWorker.serializeTaskId(taskId)
                    )
                    .build()
            )
            .build()
        workManager
            .enqueueUniqueWork(
                UNIQUE_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )

        waitUniqueWorkFinalState(workManager, UNIQUE_NAME)

        val taskState = taskStateRepository.getTaskState(taskId)
        return if (!taskState.isFinished()) {
            // Воркер мог не стартовать по каким-то причинам.
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
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(UNIQUE_NAME)
        waitUniqueWorkFinalState(workManager, UNIQUE_NAME)
        taskStateRepository.updateTaskState(taskId, TaskStateData.NotActive)
    }

    private suspend fun waitUniqueWorkFinalState(
        workManager: WorkManager,
        workUniqueName: String
    ) {
        try {
            workManager.getWorkInfosForUniqueWorkFlow(workUniqueName)
                .map { workInfos -> workInfos.maxByOrNull { it.runAttemptCount } }
                .takeWhile {
                    it != null && !it.state.isFinished
                }
                .collect()
        } catch (_: CancellationException) {
            // do nothing
        }
    }

    companion object {
        private const val UNIQUE_NAME = "ImportCardsFromZipTaskAndroidImpl"
    }
}