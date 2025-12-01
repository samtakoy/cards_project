package ru.samtakoy.importcards.domain.batch

import io.github.vinceglb.filekit.PlatformFile
import ru.samtakoy.importcards.domain.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData

interface ImportCardsZipUseCase {
    suspend fun import(
        zipFile: PlatformFile,
        opts: ImportCardsOpts,
        reportCallback: suspend (state: TaskStateData) -> Unit
    )
}