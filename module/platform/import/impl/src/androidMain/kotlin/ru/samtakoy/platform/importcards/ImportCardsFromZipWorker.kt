package ru.samtakoy.platform.importcards

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.samtakoy.data.task.TaskStateRepository
import ru.samtakoy.domain.importcards.batch.ImportCardsZipUseCase
import ru.samtakoy.domain.importcards.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.domain.task.model.TaskStateId
import ru.samtakoy.platform.notification.AndroidNotificationRepositoryImpl
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.batch_zip_import_status_cancelled
import ru.samtakoy.resources.batch_zip_import_status_init
import ru.samtakoy.resources.batch_zip_import_status_success
import ru.samtakoy.resources.common_err_message

internal class ImportCardsFromZipWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(
    appContext,
    params
), KoinComponent {

    private val importCardsUseCase: ImportCardsZipUseCase by inject()
    private val stateRepository: TaskStateRepository by inject()
    private val notificationRepository: AndroidNotificationRepositoryImpl by inject()

    private var taskId: TaskStateId? = null
    private var targetFileName: String = ""
    private var progressStatus: String = ""
    private var curProgress: Int = 0

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = notificationRepository.buildImportZipNotification(
            zipName = targetFileName,
            status = progressStatus,
            curProgress = curProgress,
            maxProgress = MAX_PROGRESS
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                notificationRepository.getImportZipNotificationId(),
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                notificationRepository.getImportZipNotificationId(),
                notification
            )
        }
    }

    override suspend fun doWork(): Result {
        taskId = deserializeTaskId(inputData.getString(PARAM_NAME_TASK_ID)!!)
        val zipFile = deserializeFile(inputData.getString(PARAM_ZIP_FILE)!!)
        targetFileName = zipFile.name

        setForeground(getForegroundInfo())

        return withContext(Dispatchers.IO) {
            try {
                reportProgress(
                    TaskStateData.ActiveStatus(
                        getString(Res.string.batch_zip_import_status_init)
                    )
                )
                importCardsUseCase.import(
                    zipFile = zipFile,
                    opts = deserializeOpts(inputData.getString(PARAM_OPTS)!!)
                ) { taskState ->
                    reportProgress(taskState)
                }
                Result.success()
            } catch (e: Exception) {
                reportProgress(
                    TaskStateData.Error(
                        e.message
                            ?: getString(Res.string.common_err_message)
                    )
                )
                Result.failure()
            }
        }
    }

    private suspend fun reportProgress(
        taskState: TaskStateData
    ) {
        stateRepository.updateTaskState(
            id = taskId!!,
            state = taskState
        )
        progressStatus = resolveProgressTitle(taskState)
        setForeground(getForegroundInfo())
    }

    private suspend fun resolveProgressTitle(
        taskState: TaskStateData
    ): String {
        return when (taskState) {
            is TaskStateData.ActiveStatus -> {
                if (taskState.progress != null) {
                    curProgress = Math.round(taskState.progress!! * 100)
                }
                taskState.message
            }
            TaskStateData.Cancelled -> {
                getString(Res.string.batch_zip_import_status_cancelled)
            }
            is TaskStateData.Error -> {
                taskState.message
            }
            TaskStateData.Success -> {
                getString(Res.string.batch_zip_import_status_success)
            }
            else -> ""
        }
    }

    companion object {
        private const val MAX_PROGRESS = 100

        const val PARAM_ZIP_FILE = "ZIP_FILE"
        const val PARAM_OPTS = "OPTS"
        const val PARAM_NAME_TASK_ID = "TASK_ID"

        fun serializeFile(file: PlatformFile): String {
            return Json.encodeToString(file)
        }

        fun deserializeFile(file: String): PlatformFile {
            return Json.decodeFromString(file)
        }

        fun serializeOpts(opts: ImportCardsOpts): String {
            return opts.name
        }

        fun deserializeOpts(opts: String): ImportCardsOpts {
            return ImportCardsOpts.valueOf(opts)
        }

        fun serializeTaskId(id: TaskStateId): String {
            return id.name
        }

        fun deserializeTaskId(id: String): TaskStateId {
            return TaskStateId.valueOf(id)
        }
    }
}