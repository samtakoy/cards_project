package ru.samtakoy.data.view

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.domain.view.ViewHistoryItem
import ru.samtakoy.domain.view.ViewHistoryItemWithInfo

interface ViewHistoryRepository {
    suspend fun addNewViewItem(qPackId: Long, cardIds: List<Long>): ViewHistoryItem
    suspend fun updateViewItem(item: ViewHistoryItem): Boolean
    fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryItem?>
    fun getViewHistoryItemsAsFlow(): Flow<List<ViewHistoryItem>>
    fun getLastViewHistoryItemForAsFlow(qPackId: Long): Flow<ViewHistoryItem?>
    suspend fun getViewHistoryItemById(viewId: Long): ViewHistoryItem?
    fun getViewHistoryItemsExAsFlow(): Flow<List<ViewHistoryItemWithInfo>>
}