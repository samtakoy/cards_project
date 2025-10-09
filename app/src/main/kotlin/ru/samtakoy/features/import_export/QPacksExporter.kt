package ru.samtakoy.features.import_export

import androidx.annotation.WorkerThread
import ru.samtakoy.features.qpack.data.QPackEntity
import ru.samtakoy.features.qpack.domain.QPack

interface QPacksExporter {
    @WorkerThread
    suspend fun exportQPackToEmail(qPackId: Long)

    @WorkerThread
    suspend fun exportQPack(qPack: QPack)

    @WorkerThread
    suspend fun exportAllToEmail()

    @WorkerThread
    suspend fun exportAllToFolder(exportDirPath: String)
}
