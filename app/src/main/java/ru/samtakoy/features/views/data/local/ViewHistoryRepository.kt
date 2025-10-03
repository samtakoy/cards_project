package ru.samtakoy.features.views.data.local

import kotlinx.coroutines.flow.Flow
import ru.samtakoy.features.views.domain.ViewHistoryItem
import ru.samtakoy.features.views.domain.ViewHistoryItemWithInfo

interface ViewHistoryRepository {
    suspend fun addNewViewItem(qPackId: Long, cardIds: List<Long>): ViewHistoryItem
    suspend fun updateViewItem(item: ViewHistoryItem): Boolean
    fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryItem?>
    fun getViewHistoryItemsAsFlow(): Flow<List<ViewHistoryItem>>
    fun getLastViewHistoryItemForAsFlow(qPackId: Long): Flow<ViewHistoryItem?>
    suspend fun getViewHistoryItemById(viewId: Long): ViewHistoryItem?
    fun getViewHistoryItemsExAsFlow(): Flow<List<ViewHistoryItemWithInfo>>
}