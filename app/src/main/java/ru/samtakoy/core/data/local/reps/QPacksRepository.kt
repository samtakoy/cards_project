package ru.samtakoy.core.data.local.reps

import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds

interface QPacksRepository {

    fun addQPack(qPack: QPackEntity): Long

    fun getQPackRx(qPackId: Long): Flowable<QPackEntity>
    suspend fun getQPack(qPackId: Long): QPackEntity?
    fun getQPackWithCardIdsAsFlow(qPackId: Long): Flow<QPackWithCardIds>;
    suspend fun deletePack(qPackId: Long)

    fun updateQPack(qPack: QPackEntity)
    fun updateQPackFavorite(qPackId: Long, favorite: Int)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: java.util.Date)

    fun isPackExists(qPackId: Long): Boolean

    fun getQPacksFromTheme(themeId: Long): List<QPackEntity>
    fun getQPacksFromThemeAsFlow(themeId: Long): Flow<List<QPackEntity>>

    suspend fun getAllQPacks(): List<QPackEntity>
    fun getAllQPacksByLastViewDateAscAsFlow(onlyFavorites: Boolean): Flow<List<QPackEntity>>
    fun getAllQPacksByLastViewDateAscFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>>
    fun getAllQPacksByCreationDateDescAsFlow(onlyFavorites: Boolean): Flow<List<QPackEntity>>
    fun getAllQPacksByCreationDateDescFilteredAsFlow(
        searchString: String,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>>
    suspend fun getAllQPacksIdsByCreationDateDescWithFavs(): List<Long>
    fun getAllQPacksIdsByCreationDateDescWithFavsAsFlow(): Flow<List<Long>>
    suspend fun getQPacksByIds(ids: List<Long>): List<QPackEntity>

}