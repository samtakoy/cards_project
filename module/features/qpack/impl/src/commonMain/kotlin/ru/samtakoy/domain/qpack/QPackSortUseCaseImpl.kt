package ru.samtakoy.domain.qpack

import kotlinx.coroutines.flow.Flow

class QPackSortUseCaseImpl(
    private val qPackRepository: QPacksRepository
) : QPackSortUseCase {
    override fun getAllQPacksByLastViewDateAscAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>> {
        return if (searchString.isNullOrBlank()) {
            qPackRepository.getAllQPacksByLastViewDateAscAsFlow(onlyFavorites = onlyFavorites)
        } else {
            qPackRepository.getAllQPacksByLastViewDateAscFilteredAsFlow(searchString, onlyFavorites = onlyFavorites)
        }
    }

    override fun getAllQPacksByCreationDateDescAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>> {
        return if (searchString.isNullOrBlank()) {
            qPackRepository.getAllQPacksByCreationDateDescAsFlow(onlyFavorites = onlyFavorites)
        } else {
            qPackRepository.getAllQPacksByCreationDateDescFilteredAsFlow(searchString, onlyFavorites = onlyFavorites)
        }
    }
}