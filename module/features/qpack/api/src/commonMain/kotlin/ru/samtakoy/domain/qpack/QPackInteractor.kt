package ru.samtakoy.domain.qpack

import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface QPackInteractor {
    suspend fun getQPack(qPackId: Long): QPack?
    fun getQPackAsFlow(qPackId: Long): Flow<QPack?>
    suspend fun addQPack(qPack: QPack): Long
    suspend fun updateQPack(qPack: QPack)
    suspend fun deleteQPack(qPackId: Long)
    @OptIn(ExperimentalTime::class)
    suspend fun updateQPackViewCount(qPackId: Long, currentTime: Instant)
    fun getChildQPacksAsFlow(themeId: Long): Flow<List<QPack>>
    suspend fun getQPacksByIds(ids: List<Long>): List<QPack>
    suspend fun getQPacksFromThemeCount(themeId: Long): Int
}