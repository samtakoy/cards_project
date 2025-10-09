package ru.samtakoy.core.domain

import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun getFavoriteCardsCount(): Int
    fun setCardFavoriteRx(cardId: Long, favorite: Int): Completable
    suspend fun setCardFavorite(cardId: Long, favorite: Int)
    fun setQPackFavorite(qPackId: Long, favorite: Int): Completable
    suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long>
    fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>>
    suspend fun getAllFavoriteCardsIds(): List<Long>
    suspend fun getAllFavoriteCardsIdsFromQPacks(qPackIds: List<Long>): List<Long>
}