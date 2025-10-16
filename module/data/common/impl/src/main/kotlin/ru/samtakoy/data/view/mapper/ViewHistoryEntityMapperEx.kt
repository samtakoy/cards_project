package ru.samtakoy.data.view.mapper

import ru.samtakoy.data.view.model.ViewHistoryEntityWithTheme
import ru.samtakoy.domain.view.ViewHistoryItemWithInfo

internal interface ViewHistoryEntityMapperEx {
    fun mapToDomain(item: ViewHistoryEntityWithTheme): ViewHistoryItemWithInfo
}

internal class ViewHistoryEntityMapperExImpl(
    private val viewHistoryEntityMapper: ViewHistoryEntityMapper
) : ViewHistoryEntityMapperEx {

    override fun mapToDomain(item: ViewHistoryEntityWithTheme): ViewHistoryItemWithInfo {
        return ViewHistoryItemWithInfo(
            viewItem = viewHistoryEntityMapper.mapToDomain(item.historyItem),
            qPackTitle = item.qPackTitle,
            themeTitle = item.themeTitle
        )
    }
}