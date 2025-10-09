package ru.samtakoy.features.views.data.local.mapper

import ru.samtakoy.features.views.data.local.model.ViewHistoryEntityWithTheme
import ru.samtakoy.features.views.domain.ViewHistoryItemWithInfo
import javax.inject.Inject

internal interface ViewHistoryEntityMapperEx {
    fun mapToDomain(item: ViewHistoryEntityWithTheme): ViewHistoryItemWithInfo
}

internal class ViewHistoryEntityMapperExImpl @Inject constructor(
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