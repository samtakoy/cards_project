package ru.samtakoy.data.qpack

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.qpack.QPack
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface QPacksRepository {

    suspend fun addQPack(qPack: QPack): Long

    suspend fun getQPack(qPackId: Long): QPack?
    fun getQPackAsFlow(qPackId: Long): Flow<QPack?>
    suspend fun deletePack(qPackId: Long)

    suspend fun updateQPack(qPack: QPack)
    suspend fun updateQPackFavorite(qPackId: Long, favorite: Int)
    @OptIn(ExperimentalTime::class)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: Instant)

    suspend fun isPackExists(qPackId: Long): Boolean

    fun getQPacksFromThemeAsFlow(themeId: Long): Flow<List<QPack>>

    suspend fun getAllQPacks(): List<QPack>
    fun getAllQPacksByLastViewDateAscAsFlow(onlyFavorites: Boolean): Flow<List<QPack>>
    fun getAllQPacksByLastViewDateAscFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPack>>
    fun getAllQPacksByCreationDateDescAsFlow(onlyFavorites: Boolean): Flow<List<QPack>>
    fun getAllQPacksByCreationDateDescFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPack>>
    suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long>
    fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>>
    suspend fun getQPacksByIds(ids: List<Long>): List<QPack>
    suspend fun getQPacksFromThemeCount(themeId: Long): Int
}