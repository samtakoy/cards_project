package ru.samtakoy.features.views.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.samtakoy.features.views.data.local.mapper.ViewHistoryEntityMapper
import ru.samtakoy.features.views.data.local.mapper.ViewHistoryEntityMapperEx
import ru.samtakoy.features.views.data.local.model.ViewHistoryDao
import ru.samtakoy.features.views.data.local.model.ViewHistoryEntity
import ru.samtakoy.features.views.domain.ViewHistoryItem
import ru.samtakoy.features.views.domain.ViewHistoryItemWithInfo
import java.util.Date
import javax.inject.Inject

internal class ViewHistoryRepositoryImpl @Inject constructor(
    private val dao: ViewHistoryDao,
    private val entityMapper: ViewHistoryEntityMapper,
    private val entityMapperEx: ViewHistoryEntityMapperEx,
) : ViewHistoryRepository {

    override suspend fun addNewViewItem(
        qPackId: Long,
        cardIds: List<Long>
    ): ViewHistoryItem {
        val result = createNewViewItem(qPackId, cardIds)
        val id = dao.add(result)
        return entityMapper.mapToDomain(result).copy(id = id)
    }

    override suspend fun updateViewItem(item: ViewHistoryItem): Boolean {
        return dao.update(entityMapper.mapToEntity(item)) > 0
    }

    override fun getViewHistoryItemAsFlow(viewItemId: Long): Flow<ViewHistoryItem?> {
        return dao.getViewHistoryItemAsFlow(viewItemId).map { item ->
            item?.let(entityMapper::mapToDomain)
        }
    }

    override fun getViewHistoryItemsAsFlow(): Flow<List<ViewHistoryItem>> {
        return dao.getAllViewHistoryItemsDescByTime().map { list ->
            list.map(entityMapper::mapToDomain)
        }
    }

    override fun getLastViewHistoryItemForAsFlow(qPackId: Long): Flow<ViewHistoryItem?> {
        return dao.getLastViewHistoryItemFor(qPackId).map {
            it?.let(entityMapper::mapToDomain)
        }
    }

    override suspend fun getViewHistoryItemById(viewId: Long): ViewHistoryItem? {
        return dao.getViewHistoryItemById(viewId = viewId)?.let(entityMapper::mapToDomain)
    }

    override fun getViewHistoryItemsExAsFlow(): Flow<List<ViewHistoryItemWithInfo>> {
        return dao.getAllViewHistoryItemsWithThemeTitleDescByTime().map { list ->
            list.map(entityMapperEx::mapToDomain)
        }
    }

    private fun createNewViewItem(
        qPackId: Long,
        cardIds: List<Long>
    ): ViewHistoryEntity {
        return ViewHistoryEntity(
            id = 0,
            qPackId = qPackId,
            viewedCardIds = emptyList(),
            todoCardIds = cardIds,
            errorCardIds = emptyList(),
            addedToFavsCardIds = emptyList(),
            restCardCount = cardIds.size,
            lastViewDate = Date()
        )
    }
}