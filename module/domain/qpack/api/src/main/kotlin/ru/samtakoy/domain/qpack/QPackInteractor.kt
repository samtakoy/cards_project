package ru.samtakoy.domain.qpack

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface QPackInteractor {
    suspend fun getQPack(qPackId: Long): QPack?
    fun getQPackAsFlow(qPackId: Long): Flow<QPack>
    suspend fun addQPack(qPack: QPack): Long
    suspend fun updateQPack(qPack: QPack)
    suspend fun deleteQPack(qPackId: Long)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date)
    fun getChildQPacksAsFlow(themeId: Long): Flow<List<QPack>>
    fun getAllQPacksByLastViewDateAscAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>>
    fun getAllQPacksByCreationDateDescAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPack>>
    suspend fun getQPacksByIds(ids: List<Long>): List<QPack>
    suspend fun getQPacksFromThemeCount(themeId: Long): Int
}