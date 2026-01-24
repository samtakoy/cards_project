package ru.samtakoy.importcards.domain

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.importcards.data.model.QPackSource

interface CardsSourceRepository {
    suspend fun getFromZip(file: PlatformFile): Flow<QPackSource>
}