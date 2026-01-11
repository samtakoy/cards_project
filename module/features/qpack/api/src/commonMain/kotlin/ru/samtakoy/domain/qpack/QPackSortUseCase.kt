package ru.samtakoy.domain.qpack

import kotlinx.coroutines.flow.Flow

interface QPackSortUseCase {
    fun getAllQPacksByLastViewDateAscAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>>

    fun getAllQPacksByCreationDateDescAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>>
}