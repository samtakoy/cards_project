package ru.samtakoy.data.view.mapper

import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.data.view.model.ViewHistoryEntity
import ru.samtakoy.domain.view.ViewHistoryItem
import kotlin.time.ExperimentalTime

internal interface ViewHistoryEntityMapper {
    fun mapToEntity(item: ViewHistoryItem): ViewHistoryEntity
    fun mapToDomain(item: ViewHistoryEntity): ViewHistoryItem
}

internal class ViewHistoryEntityMapperImpl(): ViewHistoryEntityMapper {
    override fun mapToEntity(item: ViewHistoryItem): ViewHistoryEntity {
        @OptIn(ExperimentalTime::class)
        return ViewHistoryEntity(
            id = item.id,
            qPackId = item.qPackId,
            viewedCardIds = item.viewedCardIds,
            todoCardIds = item.todoCardIds,
            errorCardIds = item.errorCardIds,
            addedToFavsCardIds = item.addedToFavsCardIds,
            restCardCount = item.todoCardIds.size,
            lastViewDate = DateUtils.dateToDbSerialized(item.lastViewDate)
        )
    }

    override fun mapToDomain(item: ViewHistoryEntity): ViewHistoryItem {
        @OptIn(ExperimentalTime::class)
        return ViewHistoryItem(
            id = item.id,
            qPackId = item.qPackId,
            viewedCardIds = item.viewedCardIds,
            todoCardIds = item.todoCardIds,
            errorCardIds = item.errorCardIds,
            addedToFavsCardIds = item.addedToFavsCardIds,
            lastViewDate = DateUtils.dateFromDbSerialized(item.lastViewDate)
        )
    }
}