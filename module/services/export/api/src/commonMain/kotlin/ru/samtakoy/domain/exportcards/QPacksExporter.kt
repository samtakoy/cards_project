package ru.samtakoy.domain.exportcards

import ru.samtakoy.domain.qpack.QPack

interface QPacksExporter {
    // @WorkerThread
    suspend fun exportQPackToEmail(qPackId: Long)

    // @WorkerThread
    suspend fun exportQPack(qPack: QPack)

    // @WorkerThread
    suspend fun exportAllToEmail()

    // @WorkerThread
    suspend fun exportAllToFolder(exportDirPath: String)
}
