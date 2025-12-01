package ru.samtakoy.data.importcards

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.data.importcards.model.QPackSource

interface CardsSourceRepository {
    suspend fun getFromZip(file: PlatformFile): Flow<QPackSource>
}