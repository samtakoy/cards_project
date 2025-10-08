package ru.samtakoy.features.qpack.data

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.qpack.domain.QPack
import java.util.Date

interface QPacksRepository {

    fun addQPack(qPack: QPack): Long

    suspend fun getQPack(qPackId: Long): QPack?
    fun getQPackAsFlow(qPackId: Long): Flow<QPack>
    suspend fun deletePack(qPackId: Long)

    fun updateQPack(qPack: QPack)
    fun updateQPackFavorite(qPackId: Long, favorite: Int)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date)

    fun isPackExists(qPackId: Long): Boolean

    fun getQPacksFromTheme(themeId: Long): List<QPack>
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