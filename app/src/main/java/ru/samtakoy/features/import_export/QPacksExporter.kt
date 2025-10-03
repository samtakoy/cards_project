package ru.samtakoy.features.import_export

import androidx.annotation.WorkerThread
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity

interface QPacksExporter {
    @WorkerThread
    suspend fun exportQPackToEmail(qPackId: Long)

    @WorkerThread
    suspend fun exportQPack(qPack: QPackEntity)

    @WorkerThread
    suspend fun exportAllToEmail()

    @WorkerThread
    suspend fun exportAllToFolder(exportDirPath: String)
}
