package ru.samtakoy.domain.importcards.batch

import io.github.vinceglb.filekit.PlatformFile
import ru.samtakoy.domain.importcards.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData

interface ImportCardsZipUseCase {
    suspend fun import(
        zipFile: PlatformFile,
        opts: ImportCardsOpts,
        reportCallback: suspend (state: TaskStateData) -> Unit
    )
}