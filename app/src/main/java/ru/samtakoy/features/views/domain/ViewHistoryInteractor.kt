package ru.samtakoy.features.views.domain

import kotlinx.coroutines.flow.Flow

interface ViewHistoryInteractor {
    suspend fun addNewViewItem(qPackId: Long, cardIds: List<Long>): ViewHistoryItem
    suspend fun addNewViewItemForPack(
        qPackId: Long,
        shuffleCards: Boolean
    ): ViewHistoryItem
    suspend fun addNewViewItemForPack(
        qPackId: Long,
        cardIds: List<Long>,
        shuffleCards: Boolean
    ): ViewHistoryItem
    suspend fun updateViewItem(item: ViewHistoryItem): Boolean
    fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryItem?>
    fun getViewHistoryItemsAsFlow(): Flow<List<ViewHistoryItem>>
    fun getLastViewHistoryItemForAsFlow(qPackId: Long): Flow<ViewHistoryItem?>
    suspend fun getViewItem(viewId: Long): ViewHistoryItem?
    fun getViewHistoryItemsWithThemeAsFlow(): Flow<List<ViewHistoryItemWithInfo>>
}