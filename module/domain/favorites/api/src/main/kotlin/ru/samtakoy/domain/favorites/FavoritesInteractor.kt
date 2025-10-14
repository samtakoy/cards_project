package ru.samtakoy.domain.favorites

import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun getFavoriteCardsCount(): Int
    suspend fun setCardFavorite(cardId: Long, favorite: Int)
    suspend fun setQPackFavorite(qPackId: Long, favorite: Int)
    suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long>
    fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>>
    suspend fun getAllFavoriteCardsIds(): List<Long>
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>
}