package ru.samtakoy.data.view.mapper

import ru.samtakoy.data.view.model.ViewHistoryEntity
import ru.samtakoy.domain.view.ViewHistoryItem
import javax.inject.Inject

internal interface ViewHistoryEntityMapper {
    fun mapToEntity(item: ViewHistoryItem): ViewHistoryEntity
    fun mapToDomain(item: ViewHistoryEntity): ViewHistoryItem
}

internal class ViewHistoryEntityMapperImpl @Inject constructor(): ViewHistoryEntityMapper {
    override fun mapToEntity(item: ViewHistoryItem): ViewHistoryEntity {
        return ViewHistoryEntity(
            id = item.id,
            qPackId = item.qPackId,
            viewedCardIds = item.viewedCardIds,
            todoCardIds = item.todoCardIds,
            errorCardIds = item.errorCardIds,
            addedToFavsCardIds = item.addedToFavsCardIds,
            restCardCount = item.todoCardIds.size,
            lastViewDate = item.lastViewDate
        )
    }

    override fun mapToDomain(item: ViewHistoryEntity): ViewHistoryItem {
        return ViewHistoryItem(
            id = item.id,
            qPackId = item.qPackId,
            viewedCardIds = item.viewedCardIds,
            todoCardIds = item.todoCardIds,
            errorCardIds = item.errorCardIds,
            addedToFavsCardIds = item.addedToFavsCardIds,
            lastViewDate = item.lastViewDate
        )
    }
}